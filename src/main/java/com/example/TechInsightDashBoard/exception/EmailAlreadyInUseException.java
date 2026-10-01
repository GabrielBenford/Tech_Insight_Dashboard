package com.example.TechInsightDashBoard.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("Email is already in use: " + email);
    }
}
