package com.fumology.carrental.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fumology.carrental.DTO.CreateRentDTO;
import com.fumology.carrental.DTO.RentDTO;
import com.fumology.carrental.enums.CarStatus;
import com.fumology.carrental.service.RentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RentController.class)
@AutoConfigureMockMvc(addFilters = false)
class RentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentService rentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRent_ShouldReturnCreatedRent_WhenInputIsValid() throws Exception {
        CreateRentDTO createRentDTO = new CreateRentDTO(
                1,
                null,
                LocalDateTime.now().plusDays(3)
        );

        RentDTO rentDTO = new RentDTO();
        rentDTO.setId(1L);
        rentDTO.setCarBrand("Mazda");
        rentDTO.setCarModel("RX-7");
        rentDTO.setEmail("test@example.com");
        rentDTO.setRentStart(LocalDateTime.now());
        rentDTO.setRentEnd(LocalDateTime.now().plusDays(3));
        rentDTO.setRentPrice(300.0);
        rentDTO.setCarStatus(CarStatus.RENTED);

        when(rentService.createRent(any(CreateRentDTO.class), eq("test@example.com"))).thenReturn(rentDTO);

        mockMvc.perform(post("/api/rents/create")
                        .principal(() -> "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.carBrand").value("Mazda"))
                .andExpect(jsonPath("$.carModel").value("RX-7"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.carStatus").value("RENTED"));

        verify(rentService, times(1)).createRent(any(CreateRentDTO.class), eq("test@example.com"));
    }

    @Test
    void createRent_ShouldReturnBadRequest_WhenInputIsInvalid() throws Exception {
        // invalid: id <= 0 and past date
        CreateRentDTO invalid = new CreateRentDTO(
                0,
                null,
                LocalDateTime.now().minusDays(1)
        );

        mockMvc.perform(post("/api/rents/create")
                        .principal(() -> "test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnRent_ShouldReturnNotFound_WhenRentDoesNotExist() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no rent with that id"))
                .when(rentService).returnRentedCar(99L, "test@example.com");

        mockMvc.perform(patch("/api/rents/{id}/return", 99L)
                        .principal(() -> "test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("There is no rent with that id")); }

    @Test
    void getRents_ShouldReturnListOfRents_WhenUserHasRents() throws Exception {
        RentDTO rent1 = new RentDTO();
        rent1.setId(1L);
        rent1.setCarBrand("Nissan");
        rent1.setCarModel("Skyline GT-R");
        rent1.setEmail("test@example.com");
        rent1.setCarStatus(CarStatus.RENTED);

        RentDTO rent2 = new RentDTO();
        rent2.setId(2L);
        rent2.setCarBrand("Toyota");
        rent2.setCarModel("Supra");
        rent2.setEmail("test@example.com");
        rent2.setCarStatus(CarStatus.RETURNED);

        when(rentService.getUserRents("test@example.com")).thenReturn(List.of(rent1, rent2));

        mockMvc.perform(get("/api/rents/all")
                        .principal(() -> "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].carBrand").value("Nissan"))
                .andExpect(jsonPath("$[1].carModel").value("Supra"))
                .andExpect(jsonPath("$[1].carStatus").value("RETURNED"));

        verify(rentService, times(1)).getUserRents("test@example.com");
    }
}