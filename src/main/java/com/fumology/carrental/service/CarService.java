package com.fumology.carrental.service;

import com.fumology.carrental.DTO.CarDTO;
import com.fumology.carrental.DTO.CarHistoryDTO;
import com.fumology.carrental.DTO.CreateCarDTO;
import com.fumology.carrental.database.entity.CarEntity;
import com.fumology.carrental.database.entity.RentEntity;
import com.fumology.carrental.database.repository.CarRepository;
import com.fumology.carrental.database.repository.RentRepository;
import com.fumology.carrental.enums.CarStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class CarService {
    private CarRepository carRepository;
    private RentRepository rentRepository;

    public CarDTO createCar(CreateCarDTO createCarDTO) {
        CarEntity carEntity = createCarDTO.mapToEntity();
        carRepository.save(carEntity);
        return new CarDTO(carEntity, CarStatus.AVAILABLE);
    }

    public CarHistoryDTO getCarHistory(int carId) {
        CarEntity carEntity = carRepository.findById(carId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found")
        );
        return new CarHistoryDTO(carEntity, checkAvailable(carEntity));
    }

    public List<CarDTO> getAllCars() {
        List<CarEntity> carEntities = carRepository.findAll();
        return carEntities.stream().map(ce -> new CarDTO(ce, checkAvailable(ce))).toList();
    }

    public CarStatus checkAvailable(CarEntity carEntity) {
        return rentRepository.findOpenRentsByCar(carEntity)
                .stream()
                .findFirst()
                .map(RentEntity::getStatus)
                .orElse(CarStatus.AVAILABLE);
    }
}
