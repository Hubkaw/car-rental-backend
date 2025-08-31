package com.fumology.carrental.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fumology.carrental.DTO.CarDTO;
import com.fumology.carrental.DTO.CreateCarDTO;
import com.fumology.carrental.database.entity.CarEntity;
import com.fumology.carrental.enums.CarStatus;
import com.fumology.carrental.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = CarController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCars_ShouldReturnListOfCars_WhenCarsExist() throws Exception {
        CarDTO skyline = new CarDTO(
                new CarEntity(1, "Nissan", "Skyline GT-R", "https://example.com/skyline.jpg", 1999, 100.0, new HashSet<>()),
                CarStatus.AVAILABLE
        );
        CarDTO supra = new CarDTO(
                new CarEntity(2, "Toyota", "Supra", "https://example.com/supra.jpg", 1998, 120.0, new HashSet<>()),
                CarStatus.AVAILABLE
        );
        List<CarDTO> cars = Arrays.asList(skyline, supra);
        when(carService.getAllCars()).thenReturn(cars);


        mockMvc.perform(get("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].brand").value("Nissan"))
                .andExpect(jsonPath("$[0].model").value("Skyline GT-R"))
                .andExpect(jsonPath("$[0].year").value(1999))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[0].availability").value("AVAILABLE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].brand").value("Toyota"))
                .andExpect(jsonPath("$[1].model").value("Supra"))
                .andExpect(jsonPath("$[1].year").value(1998))
                .andExpect(jsonPath("$[1].price").value(120.0))
                .andExpect(jsonPath("$[1].availability").value("AVAILABLE"));

        verify(carService, times(1)).getAllCars();
        verifyNoMoreInteractions(carService);
    }


    @Test
    void createCar_ShouldReturnCreatedCar_WhenInputIsValid() throws Exception {
        CreateCarDTO createCarDTO = new CreateCarDTO(
                "https://example.com/rx7.jpg",
                "Mazda",
                "RX-7",
                1995,
                110.0
        );
        CarDTO createdCar = new CarDTO(
                new CarEntity(1, "Mazda", "RX-7", "https://example.com/rx7.jpg", 1995, 110.0, new HashSet<>()),
                CarStatus.AVAILABLE
        );
        when(carService.createCar(any(CreateCarDTO.class))).thenReturn(createdCar);


        mockMvc.perform(post("/api/cars/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCarDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Mazda"))
                .andExpect(jsonPath("$.model").value("RX-7"))
                .andExpect(jsonPath("$.year").value(1995))
                .andExpect(jsonPath("$.price").value(110.0))
                .andExpect(jsonPath("$.availability").value("AVAILABLE"));

        verify(carService, times(1)).createCar(any(CreateCarDTO.class));
        verifyNoMoreInteractions(carService);
    }

    @Test
    void createCar_ShouldReturnBadRequest_WhenInputIsInvalid() throws Exception {
        CreateCarDTO invalidCarDTO = new CreateCarDTO(
                "not-a-valid-url",
                "",
                "RX-7",
                1800,
                -10.0
        );

        mockMvc.perform(post("/api/cars/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCarDTO)))
                .andExpect(status().isBadRequest());
    }

}