package com.fumology.carrental.service;

import com.fumology.carrental.DTO.CreateRentDTO;
import com.fumology.carrental.DTO.AdminCreateRentDTO;
import com.fumology.carrental.DTO.RentDTO;
import com.fumology.carrental.database.entity.CarEntity;
import com.fumology.carrental.database.entity.RentEntity;
import com.fumology.carrental.database.entity.UserEntity;
import com.fumology.carrental.database.repository.CarRepository;
import com.fumology.carrental.database.repository.RentRepository;
import com.fumology.carrental.database.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.fumology.carrental.enums.UserRole.ADMIN;

@AllArgsConstructor
@Service
public class RentService {
    private final RentRepository rentRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public List<RentDTO> getUserRents(String username) {
        UserEntity user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return rentRepository.findAllByUser(user).stream()
                .map(RentDTO::new)
                .sorted(Comparator.comparingInt(r -> r.getCarStatus().getOrder()))
                .toList();
    }

    public RentDTO createRent(CreateRentDTO createRentDTO, String username) {
        LocalDateTime start = createRentDTO.getStartDate() == null ? LocalDateTime.now() : createRentDTO.getStartDate();
        return createRentCommon(createRentDTO.getId(), start, createRentDTO.getEndDate(), username);
    }

    public RentDTO createAdminRent(AdminCreateRentDTO adminCreateRentDTO, String username) {
        UserEntity loggedInUser = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!isAdmin(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only admins can create a rent for another user");
        }
        LocalDateTime start = adminCreateRentDTO.getStartDate() == null ? LocalDateTime.now() : adminCreateRentDTO.getStartDate();
        return createRentCommon(adminCreateRentDTO.getId(), start, adminCreateRentDTO.getEndDate(), adminCreateRentDTO.getEmail());
    }

    private RentDTO createRentCommon(int carId, LocalDateTime start, LocalDateTime end, String renterEmail) {
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be before end date");
        }

        CarEntity carEntity = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No car found with that ID"));

        if (!rentRepository.findOverlappingRents(carEntity, start, end).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This car is not available during the requested period");
        }

        UserEntity renter = userRepository.findByEmailIgnoreCase(renterEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Renter user not found"));

        RentEntity rentEntity = new RentEntity();
        rentEntity.setCar(carEntity);
        rentEntity.setUser(renter);
        rentEntity.setTotalPrice(calculatePrice(start, end, carEntity));
        rentEntity.setStartTime(start);
        rentEntity.setEndTime(end);
        RentEntity saved = rentRepository.save(rentEntity);
        return new RentDTO(saved);
    }

    public void returnRentedCar(long id, String username) {
        RentEntity rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No rent found with that ID"));

        UserEntity loggedInUser = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!rent.getUser().equals(loggedInUser) && !isAdmin(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to return this rent");
        }

        if (rent.getReturnTime() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This rent is already returned");
        }

        rent.setReturnTime(LocalDateTime.now());
        rentRepository.save(rent);
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRoles().stream().anyMatch(r -> r.getRole().equals(ADMIN));
    }

    private double calculatePrice(LocalDateTime start, LocalDateTime end, CarEntity carEntity) {
        long days = Math.max(1, start.until(end, ChronoUnit.DAYS));
        return days * carEntity.getPricePerDay();
    }
}