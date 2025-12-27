package com.projectManagement.project.service.impl;

import com.projectManagement.project.service.EventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventPublisherImpl implements EventPublisher {

        @Override
        public void publish(String routingKey, Map<String, Object> payload) {
            // TODO: implement messaging / logging / event bus
            System.out.println("Publishing event: " + routingKey + " -> " + payload);
        }
}
