package com.fumology.carrental.service;

import com.fumology.carrental.DTO.CreateUserDTO;
import com.fumology.carrental.DTO.UserDTO;
import com.fumology.carrental.database.entity.RoleEntity;
import com.fumology.carrental.database.entity.UserEntity;
import com.fumology.carrental.database.repository.RoleRepository;
import com.fumology.carrental.database.repository.UserRepository;
import com.fumology.carrental.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(CreateUserDTO dto) {

        if (userRepository.findByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        RoleEntity roleEntity = roleRepository.findByRole(UserRole.USER).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        user.getRoles().add(roleEntity);

        userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).toList();
    }
}
