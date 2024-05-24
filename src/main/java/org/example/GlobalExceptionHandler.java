package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
@Slf4j
@RestControllerAdvice(basePackages = "org.example")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(true));
        log.error("Error message: {}", ex.getMessage(), ex); // 여기에 ex 추가
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> handleIllegalArgumentExceptions(IllegalArgumentException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(true));
        log.warn("IllegalArgumentException: {}", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
