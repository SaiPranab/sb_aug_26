package com.qcommerce.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qcommerce.backend.entity.Cart;
import com.qcommerce.backend.entity.UserEntity;

public interface CartRepository extends JpaRepository<Cart, String>{
    Optional<Cart> findByUser(UserEntity user);

    Optional<Cart> findByUser_UserId(String userId);

}
