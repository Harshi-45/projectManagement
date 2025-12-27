package com.projectManagement.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private Long id;

    private String name;

    private String description;

    private Long workspaceId;

    private Long ownerId;

}
