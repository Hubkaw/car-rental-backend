package com.fumology.carrental.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validUserDTO_noViolations() {
        CreateUserDTO dto = new CreateUserDTO(
                "John", "Doe", "john.doe@example.com", "Pass123"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid DTO should have no violations");
    }

    @Test
    void blankFirstName_throwsConstraintViolation() {
        CreateUserDTO dto = new CreateUserDTO(
                "", "Doe", "john.doe@example.com", "Pass123"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("You must provide your first name", violations.iterator().next().getMessage());
    }

    @Test
    void nullLastName_throwsConstraintViolation() {
        CreateUserDTO dto = new CreateUserDTO(
                "John", null, "john.doe@example.com", "Pass123"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("You must provide your last name", violations.iterator().next().getMessage());
    }

    @Test
    void invalidEmail_throwsConstraintViolation() {
        CreateUserDTO dto = new CreateUserDTO(
                "John", "Doe", "invalid-email", "Pass123"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Email must be in correct format", violations.iterator().next().getMessage());
    }

    @Test
    void blankEmail_throwsConstraintViolation() {
        CreateUserDTO dto = new CreateUserDTO(
                "John", "Doe", "", "Pass123"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Should detect blank and invalid email");
    }

    @Test
    void shortPassword_throwsConstraintViolation() {
        CreateUserDTO dto = new CreateUserDTO(
                "John", "Doe", "john.doe@example.com", "Pass"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one letter, one number and be at least 6 characters long", violations.iterator().next().getMessage());
    }

    @Test
    void passwordWithoutLetterOrNumber_throwsConstraintViolation() {
        CreateUserDTO dto = new CreateUserDTO(
                "John", "Doe", "john.doe@example.com", "password"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Password must contain at least one letter, one number and be at least 6 characters long", violations.iterator().next().getMessage());
    }

    @Test
    void multipleInvalidFields_multipleViolations() {
        CreateUserDTO dto = new CreateUserDTO(
                "", "", "invalid-email", "pass"
        );
        Set<ConstraintViolation<CreateUserDTO>> violations = validator.validate(dto);
        assertEquals(4, violations.size(), "Should detect all invalid fields");
    }
}