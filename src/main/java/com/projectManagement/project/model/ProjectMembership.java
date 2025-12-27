package com.projectManagement.project.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMembership {

    private Long id;

    private Long userId;

    private Long projectId;

    private String role;
}
