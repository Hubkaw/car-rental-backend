package com.fumology.carrental.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateRentDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRentDTO_noViolations() {
        CreateRentDTO dto = new CreateRentDTO(1,  null, LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<CreateRentDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid DTO should have no violations");
    }

    @Test
    void nonPositiveCarId_throwsConstraintViolation() {
        CreateRentDTO dto = new CreateRentDTO(0,  null, LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<CreateRentDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Car ID must be positive", violations.iterator().next().getMessage());
    }

    @Test
    void negativeCarId_throwsConstraintViolation() {
        CreateRentDTO dto = new CreateRentDTO(-1,  null, LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<CreateRentDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Car ID must be positive", violations.iterator().next().getMessage());
    }

    @Test
    void nullEndDate_throwsConstraintViolation() {
        CreateRentDTO dto = new CreateRentDTO(1,  null, null);
        Set<ConstraintViolation<CreateRentDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("End date is required", violations.iterator().next().getMessage());
    }

    @Test
    void pastEndDate_throwsConstraintViolation() {
        CreateRentDTO dto = new CreateRentDTO(1,  null, LocalDateTime.now().minusDays(1));
        Set<ConstraintViolation<CreateRentDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("End date must be in the future", violations.iterator().next().getMessage());
    }

    @Test
    void currentEndDate_throwsConstraintViolation() {
        CreateRentDTO dto = new CreateRentDTO(1, null, LocalDateTime.now());
        Set<ConstraintViolation<CreateRentDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("End date must be in the future", violations.iterator().next().getMessage());
    }

    @Test
    void multipleInvalidFields_multipleViolations() {
        CreateRentDTO dto = new CreateRentDTO(0,null, LocalDateTime.now().minusDays(1));
        Set<ConstraintViolation<CreateRentDTO>> violations = validator.validate(dto);
        assertEquals(2, violations.size(), "Should detect both invalid fields");
    }
}