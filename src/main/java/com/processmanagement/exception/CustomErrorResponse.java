package com.processmanagement.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomErrorResponse {
    private OffsetDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String errorCode;

    public static CustomErrorResponse map(ServerException ex) {
        return CustomErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode().toString())
                .status(ex.getHttpStatus())
                .build();
    }
}
