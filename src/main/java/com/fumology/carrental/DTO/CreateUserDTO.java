package com.fumology.carrental.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Data
public class CreateUserDTO {

    @NotBlank(message = "You must provide your first name")
    private String firstName;

    @NotBlank(message = "You must provide your last name")
    private String lastName;

    @NotBlank(message = "Email must be provided")
    @Email(message = "Email must be in correct format")
    private String email;

    @NotBlank(message = "You must provide a password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$", message = "Password must contain at least one letter, one number and be at least 6 characters long")
    private String password;
}
