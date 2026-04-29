package com.electronics.managerservice.service;

import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.dto.RegisterRequest;

public interface ManagerService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(RegisterRequest registerRequest);
}
