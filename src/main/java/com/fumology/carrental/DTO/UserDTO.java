package com.fumology.carrental.DTO;

import com.fumology.carrental.database.entity.UserEntity;
import com.fumology.carrental.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private boolean isAdmin;
    private List<RentDTO> rented;

    public UserDTO(UserEntity userEntity) {
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.email = userEntity.getEmail();
        this.isAdmin = userEntity.getRoles().stream().anyMatch(r -> r.getRole().equals(UserRole.ADMIN));
        this.rented = userEntity.getRentedCars().stream().map(RentDTO::new).toList();
    }
}
