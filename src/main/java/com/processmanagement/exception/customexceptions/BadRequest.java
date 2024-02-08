package com.processmanagement.exception.customexceptions;

import com.processmanagement.exception.ErrorCode;
import com.processmanagement.exception.ServerException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequest extends ServerException {

    private static String message = "Illegal argument or empty";
    private static ErrorCode errorCode = ErrorCode.BAD_REQUEST;
    private static HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public BadRequest() {
        super(message, errorCode, httpStatus);
    }

    public BadRequest(String message) {
        super(message, errorCode, httpStatus);
    }

    public BadRequest(ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }

    public BadRequest(String message, ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }

    public BadRequest(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

}
