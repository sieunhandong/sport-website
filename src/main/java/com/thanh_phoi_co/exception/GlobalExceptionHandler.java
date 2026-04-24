package com.thanh_phoi_co.exception;

import com.thanh_phoi_co.dto.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Handle validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError(400, message));
    }

    // ✅ Handle custom exception
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ResponseError> handleInvalidDataException(InvalidDataException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    // ✅ Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseError(500, "Internal server error"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseError(HttpMessageNotReadableException ex) {

        Throwable cause = ex.getCause();

        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {

            // check nếu là enum
            if (ife.getTargetType().isEnum()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseError(400,
                                "Giá trị không hợp lệ cho enum: " + ife.getValue() +
                                        ". Giá trị hợp lệ: " + Arrays.toString(ife.getTargetType().getEnumConstants()))
                );
            }
        }

        return ResponseEntity.badRequest().body("JSON không hợp lệ");
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseError> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ResponseError(
                        errorCode.getStatus(),
                        errorCode.getMessage()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseError> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseError(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                ));
    }
}