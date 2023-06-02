package com.example.processmanagementtool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ProblemDetail handleNotFound(UserNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("User not found");
        problemDetail.setType(URI.create("https://example.com/problems/user-not-found"));
//        problemDetail.setProperty("errors", List.of(ErrorDetails.API_USER_NOT_FOUND));
        problemDetail.setProperty("cokolwiek", ex.getMessage());
        return problemDetail;
    }
}
