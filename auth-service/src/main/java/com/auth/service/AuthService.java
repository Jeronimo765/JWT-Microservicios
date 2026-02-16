package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.RegisterRequest;
import com.auth.entity.User;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    User register(RegisterRequest request);
    User getUserByUsername(String username);
}