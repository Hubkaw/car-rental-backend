package com.fumology.carrental.database.repository;

import com.fumology.carrental.database.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Integer> {
}
