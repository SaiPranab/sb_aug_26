package com.qcommerce.backend.repository;

import com.qcommerce.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
