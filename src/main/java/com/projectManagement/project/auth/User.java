package com.projectManagement.project.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private Long id;

    private String email;

    private String password;

    private String name;

}
