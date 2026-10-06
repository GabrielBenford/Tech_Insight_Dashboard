package com.example.TechInsightDashBoard.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsMissingResourceToNotFound() {
        var response = handler.handleNotFound(new ResourceNotFoundException("User", 41L));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().status());
        assertEquals("User not found with id: 41", response.getBody().message());
    }

    @Test
    void mapsDuplicateEmailToConflict() {
        var response = handler.handleConflict(new EmailAlreadyInUseException("ana@example.com"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().status());
        assertEquals("Email is already in use: ana@example.com", response.getBody().message());
    }

    @Test
    void mapsValidationErrorsToBadRequestWithFieldDetails() throws Exception {
        var bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "email", "Email must be valid"));
        MethodParameter parameter = new MethodParameter(
                ValidationTarget.class.getDeclaredMethod("handle"), -1);
        var exception = new MethodArgumentNotValidException(parameter, bindingResult);

        var response = handler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email must be valid", response.getBody().fields().get("email"));
    }

    @Test
    void mapsExternalApiErrorsToBadGatewayAndTruncatesLongBody() {
        String body = "x".repeat(1200);
        var exception = new WebClientResponseException(503, "Unavailable", new HttpHeaders(),
                body.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        var response = handler.handleExternalApiError(exception);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals("503 SERVICE_UNAVAILABLE", response.getBody().fields().get("upstreamStatus"));
        assertEquals(1000, response.getBody().fields().get("upstreamBody").length());
    }

    private static class ValidationTarget {
        void handle() { }
    }
}
