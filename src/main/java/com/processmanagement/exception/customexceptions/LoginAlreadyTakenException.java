package com.processmanagement.exception.customexceptions;

import com.processmanagement.exception.ErrorCode;
import com.processmanagement.exception.ServerException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class LoginAlreadyTakenException extends ServerException {

    private static String message = "Login already taken";
    private static ErrorCode errorCode = ErrorCode.LOGIN_ALREADY_TAKEN;
    private static HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public LoginAlreadyTakenException() {
        super(message, errorCode, httpStatus);
    }

    public LoginAlreadyTakenException(String message) {
        super(message, errorCode, httpStatus);
    }

    public LoginAlreadyTakenException(ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }

    public LoginAlreadyTakenException(String message, ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }
}
