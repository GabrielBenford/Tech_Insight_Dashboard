package com.example.TechInsightDashBoard.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must have at most 100 characters")
        String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must have at most 255 characters")
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 255, message = "Password must have between 6 and 255 characters")
        String password) {
}
