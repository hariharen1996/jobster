package com.jobster.exception;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JobExceptions.class)
    public ResponseEntity<Map<String,Object>> handleJobsException(JobExceptions exceptions){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "timestamp",LocalDateTime.now(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "message",exceptions.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleException(Exception exceptions){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "timestamp",LocalDateTime.now(),
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "message",exceptions.getMessage()
        ));
    }
}
