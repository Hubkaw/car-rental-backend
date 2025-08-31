package com.fumology.carrental.DTO;

import com.fumology.carrental.database.entity.CarEntity;
import com.fumology.carrental.enums.CarStatus;
import lombok.Data;

@Data
public class CarDTO {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String imageLink;
    private CarStatus availability;

    public CarDTO(CarEntity carEntity, CarStatus availability) {
        this.id = carEntity.getId();
        this.brand = carEntity.getBrand();
        this.model = carEntity.getModel();
        this.year = carEntity.getYear();
        this.price = carEntity.getPricePerDay();
        this.availability = availability;
        this.imageLink = carEntity.getImageLink();
    }
}
