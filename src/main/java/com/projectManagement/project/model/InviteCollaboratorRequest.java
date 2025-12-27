package com.projectManagement.project.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteCollaboratorRequest {

    private String email;

    private Role role;
}
