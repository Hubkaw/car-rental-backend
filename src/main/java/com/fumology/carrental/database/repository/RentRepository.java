package com.fumology.carrental.database.repository;

import com.fumology.carrental.database.entity.CarEntity;
import com.fumology.carrental.database.entity.RentEntity;
import com.fumology.carrental.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<RentEntity, Long> {

    @Query("""
                SELECT r FROM RentEntity r 
                WHERE r.car = :car 
                AND r.returnTime IS NULL 
                ORDER BY r.startTime ASC
            """)
    List<RentEntity> findOpenRentsByCar(
            @Param("car") CarEntity car
    );

    @Query("""
                SELECT r FROM RentEntity r
                WHERE r.car = :car
                AND r.returnTime IS NULL
                AND r.startTime < :endTime
                AND r.endTime > :startTime
            """)
    List<RentEntity> findOverlappingRents(
            @Param("car") CarEntity car,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    List<RentEntity> findAllByUser(UserEntity user);
}