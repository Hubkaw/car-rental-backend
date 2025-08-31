package com.fumology.carrental.database.entity;

import com.fumology.carrental.enums.CarStatus;
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
@Setter
@Getter
public class CarEntity {

    @Id
    @GeneratedValue
    private int id;

    private String brand;
    private String model;
    private String imageLink;
    private int year;
    private double pricePerDay;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RentEntity> rentEntity = new HashSet<>();
}
