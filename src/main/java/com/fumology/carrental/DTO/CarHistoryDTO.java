package com.fumology.carrental.DTO;

import com.fumology.carrental.database.entity.CarEntity;
import com.fumology.carrental.enums.CarStatus;
import lombok.Data;

import java.util.List;

@Data
public class CarHistoryDTO {

    private CarDTO info;
    private List<RentDTO> rentHistory;

    public CarHistoryDTO(CarEntity carEntity, CarStatus status) {
        this.info = new CarDTO(carEntity, status);
        this.rentHistory = carEntity.getRentEntity().stream()
                .map(RentDTO::new)
                .toList();
    }
}
