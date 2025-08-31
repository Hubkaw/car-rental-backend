package com.fumology.carrental.database.repository;

import com.fumology.carrental.database.entity.RoleEntity;
import com.fumology.carrental.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByRole(UserRole role);
}
