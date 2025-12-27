package com.projectManagement.project.service.impl;

import com.projectManagement.project.entity.UserEntity;
import com.projectManagement.project.exception.DataValidationException;
import com.projectManagement.project.mapper.UserMapper;
import com.projectManagement.project.auth.User;
import com.projectManagement.project.repository.UserRepository;
import com.projectManagement.project.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        if(repository.existsByEmail(user.getEmail())){
            throw new DataValidationException("Mail already exists, try a new one");
        }
        UserEntity entity = mapper.toEntity(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public User update(User user, Long id) {

        UserEntity existing = repository.findByIdAndEmail(id, user.getEmail())
                .orElseThrow(() -> new DataValidationException("User Not Found"));

        // update allowed fields
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        UserEntity saved = repository.save(existing);
        return mapper.toDto(saved);
    }

}
