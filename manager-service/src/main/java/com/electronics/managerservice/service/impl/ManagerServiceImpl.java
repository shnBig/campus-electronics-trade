package com.electronics.managerservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.dto.RegisterRequest;
import com.electronics.commonserver.entity.Manager;
import com.electronics.commonserver.entity.User;
import com.electronics.commonserver.exception.BusinessException;
import com.electronics.commonserver.util.JwtUtil;
import com.electronics.commonserver.util.PasswordUtil;
import com.electronics.managerservice.mapper.ManagerMapper;
import com.electronics.managerservice.service.ManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${config.redisTimeout}")
    private Long redisTimeout;

    /**
     * 用户登录
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 根据用户名查询用户
        Manager manager = managerMapper.selectByUserName(loginRequest.getUsername());

        if (manager == null) {
            throw new RuntimeException("用户名不存在");
        }

        // 2. 验证密码（使用盐值加密）
        boolean passwordValid = PasswordUtil.verifyPassword(
                loginRequest.getPassword(),
                manager.getSalt(),
                manager.getPassword()
        );

        if (!passwordValid) {
            throw new RuntimeException("密码错误");
        }

        // 3. 检查用户状态
        if ("DISABLED".equals(manager.getStatus())) {
            throw new RuntimeException("账号已被禁用");
        }

        // 4. 生成 Token
        String token = JwtUtil.getToken(String.valueOf(manager.getId()));

        // 5. 查询用户角色
        List<String> roles = managerMapper.selectRolesByUserId(manager.getId());

        // 6. 将 Token 存入 Redis
        String tokenKey = "token:" + token;
        String userTokenKey = "user:token:" + manager.getUsername();
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(manager.getId()), redisTimeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(userTokenKey, token, redisTimeout, TimeUnit.SECONDS);

        // 7. 返回登录信息
        return new LoginResponse(
                token,
                manager.getId(),
                manager.getUsername(),
                manager.getNickName(),
                manager.getAvatar(),
                roles
        );
    }

    /**
     * 用户注册（带盐值加密）
     */
    @Override
    public LoginResponse register(RegisterRequest registerRequest) {
        // 1. 检查用户名是否已存在
        Manager existUser = managerMapper.selectByUserName(registerRequest.getUsername());
        if (existUser != null) {
            throw new BusinessException(409, "用户名已存在");
        }

        // 2. 检查手机号是否已存在
        LambdaQueryWrapper<Manager> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Manager::getPhone, registerRequest.getPhone());
        if (managerMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(409, "手机号已被注册");
        }

        // 3. 如果提供了邮箱，检查邮箱是否已存在
        if (registerRequest.getEmail() != null && !registerRequest.getEmail().isEmpty()) {
            LambdaQueryWrapper<Manager> emailWrapper = new LambdaQueryWrapper<>();
            emailWrapper.eq(Manager::getEmail, registerRequest.getEmail());
            if (managerMapper.selectCount(emailWrapper) > 0) {
                throw new BusinessException(409, "邮箱已被注册");
            }
        }

        // 4. 创建用户对象并设置属性
        Manager manager = new Manager();
        BeanUtils.copyProperties(registerRequest, manager);

        // 5. 生成盐值并加密密码
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encryptPassword(registerRequest.getPassword(), salt);

        manager.setSalt(salt);
        manager.setPassword(encryptedPassword);
        manager.setStatus("NORMAL");
        manager.setCreateTime(LocalDateTime.now());

        // 6. 插入数据库
        managerMapper.insert(manager);

        // 7. 生成 Token
        String token = JwtUtil.getToken(String.valueOf(manager.getId()));

        // 8. 将 Token 存入 Redis
        String tokenKey = "token:" + token;
        String userTokenKey = "user:token:" + manager.getUsername();
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(manager.getId()), redisTimeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(userTokenKey, token, redisTimeout, TimeUnit.SECONDS);

        // 9. 返回登录信息（新注册用户默认无角色）
        return new LoginResponse(
                token,
                manager.getId(),
                manager.getUsername(),
                manager.getNickName(),
                manager.getAvatar(),
                new ArrayList<>()
        );
    }
}