package com.processmanagement.exception.customexceptions;

import com.processmanagement.exception.ErrorCode;
import com.processmanagement.exception.ServerException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InvalidCredentialsException extends ServerException {

    private static String message = "Invalid credentials";
    private static ErrorCode errorCode = ErrorCode.INVALID_CREDENTIALS;
    private static HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public InvalidCredentialsException() {
        super(message, errorCode, httpStatus);
    }

    public InvalidCredentialsException(String message) {
        super(message, errorCode, httpStatus);
    }

    public InvalidCredentialsException(ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }

    public InvalidCredentialsException(String message, ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }
}
