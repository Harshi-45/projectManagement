package com.projectManagement.project.auth;

import java.util.Set;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JwtResponse {
    String token;
    String tokenType;
    Long userId;
    String username;
    String email;
    String role;
}

