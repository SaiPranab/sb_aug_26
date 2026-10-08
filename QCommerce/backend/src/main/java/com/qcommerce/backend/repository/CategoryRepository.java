package com.qcommerce.backend.repository;

import com.qcommerce.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByCategoryNameIgnoreCase(String categoryName);

    Page<Category> findAllByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
}
