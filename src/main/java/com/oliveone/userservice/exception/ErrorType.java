package com.oliveone.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    USER_NOT_FOUND("USER-001", "User not found with id: %d", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("USER-002", "Email already exists: %s", HttpStatus.CONFLICT);

    private final String code;
    private final String messageFormat;
    private final HttpStatus status;

    ErrorType(String code, String messageFormat, HttpStatus status) {
        this.code = code;
        this.messageFormat = messageFormat;
        this.status = status;
    }

    public BaseException throwException(Object... args) {
        return new BaseException(
            String.format(messageFormat, args),
            status,
            code
        );
    }
} 