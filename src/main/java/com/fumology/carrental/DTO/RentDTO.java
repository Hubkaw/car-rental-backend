package com.fumology.carrental.DTO;

import com.fumology.carrental.database.entity.RentEntity;
import com.fumology.carrental.enums.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentDTO {

    private long id;
    private LocalDateTime rentStart;
    private LocalDateTime rentEnd;
    private double rentPrice;
    private CarStatus carStatus;
    private LocalDateTime returnTime;
    private String carBrand;
    private String carModel;
    private String email;

    public RentDTO(RentEntity rentEntity) {
        this.id = rentEntity.getId();
        this.rentStart = rentEntity.getStartTime();
        this.rentEnd = rentEntity.getEndTime();
        this.carStatus = rentEntity.getStatus();
        this.rentPrice = rentEntity.getTotalPrice();
        this.carBrand = rentEntity.getCar().getBrand();
        this.carModel = rentEntity.getCar().getModel();
        this.email = rentEntity.getUser().getEmail();
        this.returnTime = rentEntity.getReturnTime();
    }
}
