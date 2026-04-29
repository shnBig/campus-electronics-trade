package com.electronics.userservice.service;

import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.entity.User;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(User user);
}
