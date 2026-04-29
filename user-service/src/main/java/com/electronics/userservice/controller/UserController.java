package com.electronics.userservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.UserRegisterDTO;
import com.electronics.commonserver.result.Result;
import com.electronics.commonserver.vo.UserLoginVO;
import com.electronics.commonserver.vo.UserRegisterVO;
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
    public Result<UserLoginVO> login(@RequestBody LoginRequest loginRequest) {
        log.info("登录接口:{},{}", loginRequest.getUsername(), loginRequest.getPassword());
        UserLoginVO loginResponse = userService.login(loginRequest);
        return Result.success("登录成功", loginResponse);
    }

    @PostMapping("/register")
    public Result<UserRegisterVO> register(@RequestBody UserRegisterDTO user) {
        log.info("用户注册:{}",user);
        UserRegisterVO loginResponse = userService.register(user);
        return Result.success("注册成功", loginResponse);
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
