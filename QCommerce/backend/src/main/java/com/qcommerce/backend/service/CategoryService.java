package com.qcommerce.backend.service;

import com.qcommerce.backend.dto.request.CategoryRequest;
import com.qcommerce.backend.entity.Category;
import com.qcommerce.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Page<Category> getAllCategories(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest
                .of(pageNumber, pageSize, Sort.by("categoryName"));

        return categoryRepository.findAll(pageable);
    }

    public Category createCategory(CategoryRequest categoryRequest) {
        Category newCategory = Category.builder()
                .categoryName(categoryRequest.categoryName())
                .build();

        return categoryRepository.save(newCategory);
    }
}
