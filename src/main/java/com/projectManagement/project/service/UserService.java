package com.projectManagement.project.service;


import com.projectManagement.project.auth.User;

public interface UserService  {

    User create(User user);

    User update(User user, Long id );
}
