package com.qcommerce.backend.repository;

import com.qcommerce.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserEmail(String userEmail);

    Optional<UserEntity> findByUserEmail(String userEmail);
}
