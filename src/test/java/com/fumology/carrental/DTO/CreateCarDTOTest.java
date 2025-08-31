package com.fumology.carrental.DTO;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateCarDTOTest {


    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCarDTO_noViolations() {
        CreateCarDTO dto = new CreateCarDTO(
                "https://example.com/car.jpg",
                "Toyota",
                "Camry",
                2020,
                50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid DTO should have no violations");
    }

    @Test
    void blankBrand_throwsConstraintViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                "https://example.com/car.jpg",
                "",
                "Camry",
                2020,
                50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Brand is required", violations.iterator().next().getMessage());
    }

    @Test
    void nullBrand_throwsConstraintViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                "https://example.com/car.jpg",
                null,
                "Camry",
                2020,
                50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Brand is required", violations.iterator().next().getMessage());
    }

    @Test
    void blankModel_throwsConstraintViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                "https://example.com/car.jpg",
                "Toyota",
                "",
                2020,
                50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Model is required", violations.iterator().next().getMessage());
    }

    @Test
    void yearBelowMinimum_throwsConstraintViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                "https://example.com/car.jpg",
                "Toyota",
                "Camry",
                1899,
                50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Year must be 1900 or later", violations.iterator().next().getMessage());
    }

    @Test
    void yearAboveMaximum_throwsConstraintViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                "https://example.com/car.jpg",
                "Toyota",
                "Camry",
                2026,
                50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Year cannot be after 2025", violations.iterator().next().getMessage());
    }

    @Test
    void negativePricePerDay_throwsConstraintViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                "https://example.com/car.jpg", "Toyota", "Camry", 2020, -10.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Price per day must be non-negative", violations.iterator().next().getMessage());
    }

    @Test
    void invalidImageLink_throwsConstraintViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                "invalid-url", "Toyota", "Camry", 2020, 50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Invalid URL format", violations.iterator().next().getMessage());
    }

    @Test
    void nullImageLink_noViolation() {
        CreateCarDTO dto = new CreateCarDTO(
                null, "Toyota", "Camry", 2020, 50.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Null imageLink should be allowed");
    }

    @Test
    void multipleInvalidFields_multipleViolations() {
        CreateCarDTO dto = new CreateCarDTO(
                "invalid-url", "", "", 1800, -5.0
        );
        Set<ConstraintViolation<CreateCarDTO>> violations = validator.validate(dto);
        assertEquals(5, violations.size(), "Should detect all invalid fields");
    }
}