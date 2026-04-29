package com.electronics.managerservice.controller;

import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.dto.RegisterRequest;
import com.electronics.commonserver.exception.BusinessException;
import com.electronics.commonserver.result.Result;
import com.electronics.managerservice.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("登录接口:{},{}", loginRequest.getUsername(), loginRequest.getPassword());
        LoginResponse loginResponse = managerService.login(loginRequest);
        return Result.success("登录成功", loginResponse);
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody RegisterRequest user) {
        log.info("用户注册:{}",user);
        LoginResponse loginResponse = managerService.register(user);
        return Result.success("注册成功", loginResponse);
    }
}
