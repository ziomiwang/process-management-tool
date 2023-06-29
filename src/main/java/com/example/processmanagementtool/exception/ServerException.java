package com.example.processmanagementtool.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class ServerException extends RuntimeException {

    private String message;
    private ErrorCode errorCode;
    private HttpStatus httpStatus;

    private static HttpStatus defaultHttpStatus = HttpStatus.NOT_FOUND;

    public ServerException(String message) {
        this.message = message;
    }

    public ServerException(String message, ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatus = defaultHttpStatus;
    }

    public ServerException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
