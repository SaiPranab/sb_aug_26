package com.qcommerce.backend.repository;

import com.qcommerce.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserEmail(String userEmail);
}
