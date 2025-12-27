package com.projectManagement.project.mapper;

import com.projectManagement.project.entity.UserEntity;
import com.projectManagement.project.auth.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserEntity, User>{


}
