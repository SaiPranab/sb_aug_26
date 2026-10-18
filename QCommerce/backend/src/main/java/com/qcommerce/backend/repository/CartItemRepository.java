package com.qcommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qcommerce.backend.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, String>{

}

