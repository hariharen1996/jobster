package com.jobster.service;

import com.jobster.dto.request.LoginRequest;
import com.jobster.dto.request.RegisterRequest;
import com.jobster.dto.response.LoginResponse;

public interface UserService {
    void register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
