package com.qcommerce.backend.mapper;


import com.qcommerce.backend.dto.response.CategoryResponse;
import com.qcommerce.backend.entity.Category;

public class CategoryMapper {
    private CategoryMapper() {}

//    Entity to DTO
    public static CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getCategoryImage(),
                category.getItems() == null ? 0 : category.getItems().size()
        );

        return response;
    }
}
