package com.projectManagement.project.controller;

import com.projectManagement.project.auth.User;
import com.projectManagement.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User createdUser = userService.create(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
//    @RequireRole({Role.OWNER})
    public ResponseEntity<User> update(@Valid @RequestBody User user,
                                       @PathVariable Long id) {
        User updatedUser = userService.update(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
