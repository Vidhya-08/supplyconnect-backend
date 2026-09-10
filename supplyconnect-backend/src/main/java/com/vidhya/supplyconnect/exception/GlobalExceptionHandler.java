package com.vidhya.supplyconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST; //400

        String message = ex.getMessage();

        if (message != null &&
                (message.contains("not found")
                        || message.contains("Not found"))) {
            status = HttpStatus.NOT_FOUND; //404
        }

        if (message != null &&
                (message.contains("not authorized")
                        || message.contains("not associated"))) {
            status = HttpStatus.FORBIDDEN; //403
        }

        ErrorResponse error = new ErrorResponse(
                status.value(),
                message,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }
}