package com.projectManagement.project.exception;

import ch.qos.logback.core.util.StringUtil;
import com.projectManagement.project.model.ErrorResponse;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

public abstract class BaseException extends RuntimeException{

    private String errorCode;
    private List<Error> errorList;

    public BaseException(String errorCode) {
        this(errorCode, (String)null);
    }

    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorResponse getErrorResponse(MessageSource messageSource) {
        return createErrorResponse(this.getErrorCode(), this.getMessage(), messageSource);
    }

    public BaseException(String errorCode, String message, List<Error> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errorList = errors;
    }

    public static ErrorResponse createErrorResponse(String errorCode, String errorMessage, MessageSource messageSource) {
        String message = messageSource.getMessage(errorCode, (Object[])null, errorMessage, Locale.ENGLISH);
        if (StringUtil.isNullOrEmpty(message)) {
            message = "The requested resource is not found.";
        }

        return new ErrorResponse(errorCode, message);
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public List<Error> getErrorList() {
        return this.errorList;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorList(final List<Error> errorList) {
        this.errorList = errorList;
    }

}
