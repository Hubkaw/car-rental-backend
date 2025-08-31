package com.fumology.carrental.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminCreateRentDTO {

    @Min(value = 1, message = "Car ID must be positive")
    private int id;

    @Email(message = "Email must be a valid Email")
    @NotNull(message = "Email must be provided")
    private String email;

    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;
}
