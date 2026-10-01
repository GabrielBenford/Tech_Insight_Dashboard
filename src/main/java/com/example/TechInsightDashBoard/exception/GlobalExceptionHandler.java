package com.example.TechInsightDashBoard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ApiError> handleConflict(EmailAlreadyInUseException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fields.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "Request validation failed", fields);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiError> handleExternalApiError(WebClientResponseException exception) {
        String body = exception.getResponseBodyAsString();
        if (body.length() > 1000) {
            body = body.substring(0, 1000);
        }

        Map<String, String> details = Map.of(
                "upstreamStatus", exception.getStatusCode().toString(),
                "upstreamBody", body
        );
        return response(HttpStatus.BAD_GATEWAY,
                "The external jobs API rejected the request.", details);
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String message, Map<String, String> fields) {
        ApiError body = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, fields);
        return ResponseEntity.status(status).body(body);
    }
}
