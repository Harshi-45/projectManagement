package com.projectManagement.project.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<?> handleValidationException(DataValidationException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("code", ex.getErrorCode());
        body.put("message", ex.getCause());
        body.put("timestamp", Instant.now());

        return ResponseEntity.badRequest().body(body);
    }
}
