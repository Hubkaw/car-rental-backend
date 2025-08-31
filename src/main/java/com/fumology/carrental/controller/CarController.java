package com.fumology.carrental.controller;

import com.fumology.carrental.DTO.CarDTO;
import com.fumology.carrental.DTO.CreateCarDTO;
import com.fumology.carrental.service.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @GetMapping("/api/cars")
    public List<CarDTO> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping("/api/cars/new")
    public CarDTO createCar(@Valid @RequestBody CreateCarDTO createCarDTO) {
        return carService.createCar(createCarDTO);
    }
}
