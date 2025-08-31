package com.fumology.carrental.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    private String email;

    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            inverseJoinColumns = {@JoinColumn(referencedColumnName = "id")},
            joinColumns = {@JoinColumn(referencedColumnName = "email")}
    )
    @JsonIgnore
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RentEntity> rentedCars = new HashSet<>();
}
