package com.fumology.carrental.database.entity;

import com.fumology.carrental.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"role"}))
public class RoleEntity {

    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
