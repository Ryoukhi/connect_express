package com.eadl.connect_backend.infrastructure.adapter.in.web.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eadl.connect_backend.domain.port.exception.AdminNotFoundException;
import com.eadl.connect_backend.domain.port.exception.BusinessException;
import com.eadl.connect_backend.domain.port.exception.DocumentNotFoundException;
import com.eadl.connect_backend.domain.port.exception.UserNotFoundException;

import jakarta.persistence.EntityNotFoundException;

/**
 * Gestion globale des exceptions
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            List<String> details
    ) {}

    // ===========================
    // Business exceptions
    // ===========================
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                List.of(ex.getClass().getName())
        );
        return ResponseEntity.badRequest().body(error);
    }

    // ===========================
    // Validation @Valid
    // ===========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                errors
        );
        return ResponseEntity.badRequest().body(error);
    }

    // ===========================
    // Illegal arguments
    // ===========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                List.of(ex.getClass().getName())
        );
        return ResponseEntity.badRequest().body(error);
    }

    // ===========================
    // Not found exceptions
    // ===========================
    @ExceptionHandler({UserNotFoundException.class, AdminNotFoundException.class,
                       DocumentNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                List.of(ex.getClass().getName())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ===========================
    // Toutes les autres exceptions
    // ===========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                List.of(ex.getClass().getName())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}