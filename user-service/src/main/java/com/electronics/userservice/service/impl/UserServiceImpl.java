package com.electronics.userservice.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.dto.RegisterRequest;
import com.electronics.commonserver.dto.UserRegisterDTO;
import com.electronics.commonserver.entity.User;
import com.electronics.commonserver.exception.BusinessException;
import com.electronics.commonserver.util.JwtUtil;
import com.electronics.commonserver.util.PasswordUtil;
import com.electronics.commonserver.vo.UserLoginVO;
import com.electronics.commonserver.vo.UserRegisterVO;
import com.electronics.userservice.mapper.UserMapper;
import com.electronics.userservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
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
    public UserLoginVO login(LoginRequest loginRequest) {
        User user = userMapper.selectByUserName(loginRequest.getUsername());

        if (user == null) {
            throw new BusinessException(404, "用户名不存在");
        }

        boolean passwordValid = PasswordUtil.verifyPassword(
                loginRequest.getPassword(),
                user.getSalt(),
                user.getPassword()
        );

        if (!passwordValid) {
            throw new BusinessException(401, "密码错误");
        }

        if (user.getStatus() != null && user.getStatus() == 2) {
            throw new BusinessException(403, "账号已被禁用");
        }

        String token = JwtUtil.getToken(String.valueOf(user.getId()));


        String tokenKey = "token:" + token;
        String userTokenKey = "user:token:" + user.getUsername();

        redisTemplate.opsForValue().multiSet(Map.of(
                tokenKey, String.valueOf(user.getId()),
                userTokenKey, token
        ));
        redisTemplate.expire(tokenKey, redisTimeout, TimeUnit.SECONDS);
        redisTemplate.expire(userTokenKey, redisTimeout, TimeUnit.SECONDS);

        return new UserLoginVO(
                token,
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getGender(),
                user.getStatus(),
                user.getAvatar()
        );
    }
    /**
     * 用户注册（带盐值加密）
     */
    @Override
    public UserRegisterVO register(UserRegisterDTO registerRequest) {
        // 1. 检查用户名是否已存在
        User existUser = userMapper.selectByUserName(registerRequest.getUsername());
        if (existUser != null) {
            throw new BusinessException(409, "用户名已存在");
        }

        // 2. 检查手机号是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, registerRequest.getPhone());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(409, "手机号已被注册");
        }

        // 3. 如果提供了邮箱，检查邮箱是否已存在
        if (registerRequest.getEmail() != null && !registerRequest.getEmail().isEmpty()) {
            LambdaQueryWrapper<User> emailWrapper = new LambdaQueryWrapper<>();
            emailWrapper.eq(User::getEmail, registerRequest.getEmail());
            if (userMapper.selectCount(emailWrapper) > 0) {
                throw new BusinessException(409, "邮箱已被注册");
            }
        }

        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);

        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encryptPassword(registerRequest.getPassword(), salt);

        user.setSalt(salt);
        user.setPassword(encryptedPassword);
        user.setStatus(1);
        user.setGender(1);
        user.setCreateTime(LocalDateTime.now());

        userMapper.insert(user);

        // 7. 生成 Token
        String token = JwtUtil.getToken(String.valueOf(user.getId()));

        // 8. 将 Token 存入 Redis
        String tokenKey = "token:" + token;
        String userTokenKey = "user:token:" + user.getUsername();
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(user.getId()), redisTimeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(userTokenKey, token, redisTimeout, TimeUnit.SECONDS);

        // 9. 返回登录信息（新注册用户默认无角色）
        return new UserRegisterVO(
                token,
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatar(),
                user.getGender(),
                user.getCreditScore(),
                user.getStatus()
        );
    }
}
