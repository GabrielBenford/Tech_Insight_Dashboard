package com.example.TechInsightDashBoard.DTO;

import com.example.TechInsightDashBoard.DTO.TechDTO.TechRequestDTO;
import com.example.TechInsightDashBoard.DTO.UserDTO.UserRequestDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestValidationTest {

    private static Validator validator;
    private static jakarta.validation.ValidatorFactory factory;

    @BeforeAll
    static void createValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        factory.close();
    }

    @Test
    void userRequestRequiresValidNameEmailAndPassword() {
        var violations = validator.validate(new UserRequestDTO(" ", "not-an-email", "123"));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void userRequestAcceptsValidValues() {
        assertTrue(validator.validate(new UserRequestDTO("Ana", "ana@example.com", "password123")).isEmpty());
    }

    @Test
    void technologyRequestRequiresNonblankNameAndHistory() {
        var violations = validator.validate(new TechRequestDTO(" ", ""));
        assertEquals(2, violations.size());
    }

    @Test
    void technologyNameCannotExceedConfiguredLimit() {
        String tooLongName = "x".repeat(151);
        assertTrue(validator.validate(new TechRequestDTO(tooLongName, "history")).stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("techName")));
    }
}
