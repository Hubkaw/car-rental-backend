package com.fumology.carrental.database.entity;


import com.fumology.carrental.enums.CarStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rent_entity")
public class RentEntity {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;
    private LocalDateTime returnTime;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double totalPrice;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private CarEntity car;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private UserEntity user;

    @Transient
    public CarStatus getStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (returnTime != null) {
            return CarStatus.RETURNED;
        } else if (now.isBefore(startTime)) {
            return CarStatus.SCHEDULED;
        } else if (now.isAfter(endTime)) {
            return CarStatus.OVERDUE;
        } else {
            return CarStatus.RENTED;
        }
    }
}
