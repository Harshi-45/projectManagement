package com.projectManagement.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "jobs")
public class JobEntity extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private Long userId;

    private String status;

    private Long retryCount;

    private String requestPayload;

    private String resultPayload;

    private String idempotencyKey;


}
