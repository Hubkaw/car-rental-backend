package com.fumology.carrental.service;

import com.fumology.carrental.DTO.CreateRentDTO;
import com.fumology.carrental.DTO.RentDTO;
import com.fumology.carrental.database.entity.CarEntity;
import com.fumology.carrental.database.entity.RentEntity;
import com.fumology.carrental.database.entity.RoleEntity;
import com.fumology.carrental.database.entity.UserEntity;
import com.fumology.carrental.database.repository.CarRepository;
import com.fumology.carrental.database.repository.RentRepository;
import com.fumology.carrental.database.repository.RoleRepository;
import com.fumology.carrental.database.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.fumology.carrental.enums.UserRole.ADMIN;
import static com.fumology.carrental.enums.UserRole.USER;

@Component
@AllArgsConstructor
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private RentRepository rentRepository;
    private RentService rentService;
    private PasswordEncoder passwordEncoder;

    private static final String exampleUserEmail = "user@ano.ninja";
    private static final String exampleAdminEmail = "admin@ano.ninja";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        RoleEntity userRole = roleRepository.findByRole(USER).orElseGet(() -> roleRepository.save(new RoleEntity(1, USER)));
        RoleEntity adminRole = roleRepository.findByRole(ADMIN).orElseGet(() -> roleRepository.save(new RoleEntity(2, ADMIN)));

        UserEntity exampleUser = userRepository.findById(exampleUserEmail).orElseGet(() -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(exampleUserEmail);
            userEntity.setPassword(passwordEncoder.encode("user"));
            userEntity.setFirstName("Marek");
            userEntity.setLastName("Userski");
            userEntity.getRoles().add(userRole);
            return userRepository.save(userEntity);
        });

        UserEntity adminUser = userRepository.findById(exampleAdminEmail).orElseGet(() -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(exampleAdminEmail);
            userEntity.setPassword(passwordEncoder.encode("admin"));
            userEntity.setFirstName("Czarek");
            userEntity.setLastName("Admiński");
            userEntity.getRoles().addAll(List.of(userRole, adminRole));
            return userRepository.save(userEntity);
        });

        if (carRepository.findAll().size() < 4) {

            Map<String, String> images = Map.of("Toyota", "https://garagedreams.net/wp-content/uploads/2019/08/Toyota-Supra-Mk4.jpg",
                    "Nissan", "https://images.classic.com/vehicles/62f783a0daf5fc3949a78570332cd4592c640def.jpg?auto=format&fit=crop&ar=16:9",
                    "Mazda", "https://static1.hotcarsimages.com/wordpress/wp-content/uploads/2023/07/mazda-rx-7-spirt-r-front-quarter-view.jpg",
                    "Honda", "https://images.caradisiac.com/logos-ref/modele/modele--honda-integra/S7-modele--honda-integra.jpg");
            Map.of("Toyota", "Supra", "Nissan", "GTR", "Mazda", "RX-7", "Honda", "Integra").forEach((k, v) -> {
                CarEntity car = new CarEntity();
                car.setBrand(k);
                car.setModel(v);
                car.setImageLink(images.get(k));
                car.setPricePerDay(200 + Math.random() * 100);
                car.setYear(1995);
                carRepository.save(car);
            });
        }
        if (rentRepository.findAll().size() < 3) {
            List<CarEntity> cars = carRepository.findAll().stream().limit(3).toList();
            List<UserEntity> users = userRepository.findAll().stream().limit(2).toList();
            rentService.createRent(new CreateRentDTO(cars.get(0).getId(), LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusDays(2)), users.get(0).getEmail());
            rentService.createRent(new CreateRentDTO(cars.get(1).getId(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5)), users.get(1).getEmail());
            rentService.createRent(new CreateRentDTO(cars.get(2).getId(), LocalDateTime.now().minusHours(10), LocalDateTime.now().plusDays(5)), users.get(0).getEmail());

        }

    }
}
