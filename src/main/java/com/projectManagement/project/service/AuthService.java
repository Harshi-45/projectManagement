package com.projectManagement.project.service;

import com.projectManagement.project.auth.JwtResponse;
import com.projectManagement.project.model.LoginRequest;
import com.projectManagement.project.auth.RegisterRequest;
import com.projectManagement.project.auth.User;


public interface AuthService {
    User register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}

