package com.electronics.userservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.entity.User;
import com.electronics.commonserver.exception.BusinessException;
import com.electronics.commonserver.result.Result;
import com.electronics.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("登录接口:{},{}", loginRequest.getUsername(), loginRequest.getPassword());
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            return Result.success("登录成功", loginResponse);
        } catch (BusinessException e) {
            log.error("登录失败: {}", e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage());
            return Result.error(500, "系统错误");
        }
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody User user) {
        try {
            LoginResponse loginResponse = userService.register(user);
            return Result.success("注册成功", loginResponse);
        } catch (BusinessException e) {
            log.error("注册失败: {}", e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("注册异常: {}", e.getMessage());
            return Result.error(500, "系统错误");
        }
    }

    @GetMapping("/testHot")
    @SentinelResource(value = "testHot", blockHandler = "testHotBlockHandler")
    public String testHot(@RequestParam(value = "id", required = false) String id,
                         @RequestParam(value = "name", required = false) String name) {
        return id + name;
    }

    public String testHotBlockHandler(String id, String name) {
        return "限流了";
    }
}
