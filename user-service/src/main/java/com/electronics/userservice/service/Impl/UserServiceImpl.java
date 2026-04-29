package com.electronics.userservice.service.Impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.entity.User;
import com.electronics.commonserver.exception.BusinessException;
import com.electronics.commonserver.util.JwtUtil;
import com.electronics.commonserver.util.PasswordUtil;
import com.electronics.userservice.mapper.UserMapper;
import com.electronics.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${config.redisTimeout:3600}")
    private Long redisTimeout;

    /**
     * 用户登录
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 根据用户名查询用户
        User user = userMapper.selectByUserName(loginRequest.getUsername());

        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }

        // 2. 验证密码（使用盐值加密）
        boolean passwordValid = PasswordUtil.verifyPassword(
                loginRequest.getPassword(),
                user.getSalt(),
                user.getPassword()
        );

        if (!passwordValid) {
            throw new RuntimeException("密码错误");
        }

        // 3. 检查用户状态
        if ("DISABLED".equals(user.getStatus())) {
            throw new RuntimeException("账号已被禁用");
        }

        // 4. 生成 Token
        String token = JwtUtil.getToken(String.valueOf(user.getUserId()));

        // 5. 查询用户角色
        List<String> roles = userMapper.selectRolesByUserId(user.getUserId());

        // 6. 将 Token 存入 Redis
        String tokenKey = "token:" + token;
        String userTokenKey = "user:token:" + user.getUserName();
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(user.getUserId()), redisTimeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(userTokenKey, token, redisTimeout, TimeUnit.SECONDS);

        // 7. 返回登录信息
        return new LoginResponse(
                token,
                user.getUserId(),
                user.getUserName(),
                user.getNickName(),
                user.getAvatar(),
                roles
        );
    }

    /**
     * 用户注册（带盐值加密）
     */
    @Override
    public LoginResponse register(User user) {
        // 1. 检查用户名是否已存在
        User existUser = userMapper.selectByUserName(user.getUserName());
        if (existUser != null) {
            throw new BusinessException(409, "用户名已存在");
        }

        // 2. 检查手机号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, user.getPhone());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(409, "手机号已被注册");
        }

        // 3. 生成盐值并加密密码
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encryptPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setPassword(encryptedPassword);
        user.setStatus("NORMAL");
        user.setCreateUser(LocalDateTime.now());

        // 4. 插入数据库
        userMapper.insert(user);

        // 5. 生成 Token
        String token = JwtUtil.getToken(String.valueOf(user.getUserId()));

        // 6. 将 Token 存入 Redis
        String tokenKey = "token:" + token;
        String userTokenKey = "user:token:" + user.getUserName();
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(user.getUserId()), redisTimeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(userTokenKey, token, redisTimeout, TimeUnit.SECONDS);

        // 7. 返回登录信息（新注册用户默认无角色）
        return new LoginResponse(
                token,
                user.getUserId(),
                user.getUserName(),
                user.getNickName(),
                user.getAvatar(),
                new ArrayList<>()
        );
    }
}
