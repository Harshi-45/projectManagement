package com.projectManagement.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    private Long id;

    private Long projectId;

    private Long userId;

    private String status;

    private Long retryCount;

    private String requestPayload;

    private String resultPayload;

    private String idempotencyKey;


}
