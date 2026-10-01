package com.example.TechInsightDashBoard.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TechRequestDTO(
        @NotBlank(message = "Technology name is required")
        @Size(max = 150, message = "Technology name must have at most 150 characters")
        String techName,
        @NotBlank(message = "History is required")
        String history) {
}
