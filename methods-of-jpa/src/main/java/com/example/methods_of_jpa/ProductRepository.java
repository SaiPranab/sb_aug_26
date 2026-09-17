package com.example.methods_of_jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  Optional<Product> findByProductName(String name);

  List<Product> findAllByProductPriceBetween(double startPrice, double endPrice);
}
