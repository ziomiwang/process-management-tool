package com.processmanagement.exception.customexceptions;

import com.processmanagement.exception.ErrorCode;
import com.processmanagement.exception.ServerException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TeamNotFoundException extends ServerException {

    private static String message = "Team not found";
    private static ErrorCode errorCode = ErrorCode.TEAM_NOT_FOUND;
    private static HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public TeamNotFoundException() {
        super(message, errorCode, httpStatus);
    }

    public TeamNotFoundException(String message) {
        super(message, errorCode, httpStatus);
    }

    public TeamNotFoundException(ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }

    public TeamNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode, httpStatus);
    }
}
