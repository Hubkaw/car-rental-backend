package com.fumology.carrental.controller;

import com.fumology.carrental.DTO.CreateUserDTO;
import com.fumology.carrental.DTO.TokenResponseDTO;
import com.fumology.carrental.DTO.UserDTO;
import com.fumology.carrental.configuration.Security.JWTService;
import com.fumology.carrental.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
public class SecurityController {

    private final JWTService jwtService;
    private final UserService userService;

    @GetMapping("/api/login")
    public TokenResponseDTO login(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new TokenResponseDTO(jwtService.generateToken(authentication), "Bearer");
    }

    @PostMapping("/api/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody CreateUserDTO dto) {
        userService.createUser(dto);
    }

    @GetMapping("/api/users/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
