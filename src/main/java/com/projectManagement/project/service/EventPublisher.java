package com.projectManagement.project.service;

import org.springframework.stereotype.Component;

import java.util.Map;

public interface EventPublisher {
    void publish(String routingKey, Map<String, Object> payload);
}

