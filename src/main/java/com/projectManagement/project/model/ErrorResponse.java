package com.projectManagement.project.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = -2657441832258409759L;
    private final String code;
    private final String message;
    @JsonInclude(Include.NON_EMPTY)
    private final List<Error> errors;
    private final Date timestamp;

    public ErrorResponse(final String code, final String message) {
        this(code, message, (List)null);
    }

    public ErrorResponse(final String code, final String message, final List<Error> errors) {
        this.errors = new ArrayList();
        this.timestamp = new Date();
        this.code = code;
        this.message = message;
        if (errors != null) {
            this.errors.addAll(errors);
        }

    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public List<Error> getErrors() {
        return this.errors;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }
}
