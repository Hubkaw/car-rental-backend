package com.fumology.carrental.DTO;


import com.fumology.carrental.database.entity.CarEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.URL;


@AllArgsConstructor
@Data
public class CreateCarDTO {

    @URL(message = "Invalid URL format")
    private String imageLink;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1900, message = "Year must be 1900 or later")
    @Max(value = 2025, message = "Year cannot be after 2025")
    private int year;

    @Min(value = 0, message = "Price per day must be non-negative")
    private double pricePerDay;

    public CarEntity mapToEntity() {
        CarEntity carEntity = new CarEntity();
        carEntity.setBrand(this.brand);
        carEntity.setModel(this.model);
        carEntity.setYear(this.year);
        carEntity.setPricePerDay(this.pricePerDay);
        carEntity.setImageLink(this.imageLink);
        return carEntity;
    }
}
