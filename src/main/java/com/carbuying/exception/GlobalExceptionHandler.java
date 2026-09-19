package com.carbuying.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String PATH = "path";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(
            NotFoundException ex,
            WebRequest request) {
        log.warn("NotFoundException: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.NOT_FOUND.value());
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(
            BusinessException ex,
            WebRequest request) {
        log.warn("BusinessException: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());

        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(MESSAGE, "Validation failed");
        body.put("errors", errors);
        body.put(PATH, request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected error: ", ex);

        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put(MESSAGE, "An unexpected error occurred");
        body.put(PATH, request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
