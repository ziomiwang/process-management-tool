package com.processmanagement.exception.customexceptions;

import com.processmanagement.exception.ErrorCode;
import com.processmanagement.exception.ServerException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UserNotFoundException extends ServerException {

    private static String message = "User not found";
    private static ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
    private static HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public UserNotFoundException() {
        super(message, errorCode, httpStatus);
    }

    public UserNotFoundException(String message) {
        super(message, errorCode, httpStatus);
    }

    public UserNotFoundException(ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }

    public UserNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }
}
