package com.projectManagement.project.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.projectManagement.project.entity.UserEntity;
import com.projectManagement.project.exception.DataValidationException;
import com.projectManagement.project.mapper.UserMapper;
import com.projectManagement.project.auth.JwtResponse;
import com.projectManagement.project.model.LoginRequest;
import com.projectManagement.project.auth.RegisterRequest;
import com.projectManagement.project.auth.User;
import com.projectManagement.project.repository.UserRepository;
import com.projectManagement.project.security.CustomUserDetails;
import com.projectManagement.project.security.JwtTokenProvider;
import com.projectManagement.project.service.AuthService;
import com.projectManagement.project.service.EventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final UserMapper mapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final EventPublisher eventPublisher;
    @Value("${rabbitmq.registration.routing-key}")
    private String registrationRoutingKey;
    @Value("${rabbitmq.login.routing-key}")
    private String loginRoutingKey;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DataValidationException("Bad Request", "Email already in use");
        }


        UserEntity user = UserEntity.builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user.setCreatedBy("SYSTEM");
        user.setModifiedBy("SYSTEM");
        user.setModifiedAt(Instant.now());
        user.setCreatedAt(Instant.now());
        UserEntity saved = userRepository.save(user);
        publishEvent(registrationRoutingKey, saved, "registration");
        return mapper.toDto(saved);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        UserEntity user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new DataValidationException("UNAUTHORIZED", "Invalid credentials") {
                });
//        user.set(Instant.now());
        userRepository.save(user);
        publishEvent(loginRoutingKey, user, "login");
        String token = jwtTokenProvider.generateToken(authentication, user);
        return JwtResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }


    private void publishEvent(String routingKey, UserEntity user, String type) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", type);
        payload.put("userId", user.getId());
        payload.put("email", user.getEmail());
        payload.put("timestamp", Instant.now().toString());
        eventPublisher.publish(routingKey, payload);
    }
}

