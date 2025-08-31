package com.fumology.carrental.controller;

import com.fumology.carrental.DTO.AdminCreateRentDTO;
import com.fumology.carrental.DTO.CreateRentDTO;
import com.fumology.carrental.DTO.RentDTO;
import com.fumology.carrental.service.RentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
public class RentController {

    private final RentService rentService;

    @PostMapping("/api/rents/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RentDTO rentCar(Principal principal, @Valid @RequestBody CreateRentDTO createRentDTO) {
        return rentService.createRent(createRentDTO, principal.getName());
    }

    @PostMapping("/api/rents/admin/create")
    public RentDTO adminRentCar(Principal principal, @Valid @RequestBody AdminCreateRentDTO adminCreateRentDTO) {
        return rentService.createAdminRent(adminCreateRentDTO, principal.getName());
    }
    @PatchMapping("/api/rents/{id}/return")
    public String returnRent(Principal principal,  @PathVariable Long id) {
        rentService.returnRentedCar(id, principal.getName());
        return "Car Returned";
    }

    @GetMapping("/api/rents/all")
    public List<RentDTO> getRents(Principal principal) {
        return rentService.getUserRents(principal.getName());
    }
}
