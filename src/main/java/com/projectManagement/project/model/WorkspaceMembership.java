package com.projectManagement.project.model;


import com.projectManagement.project.entity.UserEntity;
import com.projectManagement.project.entity.WorkSpaceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceMembership {
    private Long id;

    private WorkSpaceEntity workspace;

    private UserEntity user;

    private Role role;

}
