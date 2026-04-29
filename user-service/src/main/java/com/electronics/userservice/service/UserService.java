package com.electronics.userservice.service;

import com.electronics.commonserver.dto.LoginRequest;
import com.electronics.commonserver.dto.LoginResponse;
import com.electronics.commonserver.dto.RegisterRequest;
import com.electronics.commonserver.dto.UserRegisterDTO;
import com.electronics.commonserver.vo.UserLoginVO;
import com.electronics.commonserver.vo.UserRegisterVO;

public interface UserService {
    UserLoginVO login(LoginRequest loginRequest);
    UserRegisterVO register(UserRegisterDTO registerRequest);
}
