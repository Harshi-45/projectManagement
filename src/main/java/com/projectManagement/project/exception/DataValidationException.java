package com.projectManagement.project.exception;

public class DataValidationException extends BaseException{

    public DataValidationException(String errorCode) {
        super(errorCode);
    }

    public DataValidationException(String errorCode, String message) {
        super(errorCode, message);
    }
}
