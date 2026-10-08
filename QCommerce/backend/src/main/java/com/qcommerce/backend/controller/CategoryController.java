package com.qcommerce.backend.controller;

import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.dto.request.CategoryRequest;
import com.qcommerce.backend.dto.response.CategoryResponse;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.entity.Category;
import com.qcommerce.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public PageResponse<Category> getAllCategories(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber
    ) {
        return categoryService.getAllCategories(search, pageNumber, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@RequestBody @Valid CategoryRequest categoryRequest){
        return categoryService.createCategory(categoryRequest);
    }

    @GetMapping("/{categoryId}")
    public CategoryResponse getCategory(@PathVariable String categoryId){
        return categoryService.getCategoryById(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategoryById(categoryId);
    }

    @PutMapping("/{categoryId}")
    public CategoryResponse updateCategory(
            @PathVariable String categoryId,
            @RequestBody @Valid CategoryRequest categoryRequest) {
        return categoryService.updateCategoryById(categoryId, categoryRequest);
    }

    @PostMapping("/{categoryId}/image")
    public CategoryResponse uploadImage(
            @PathVariable String categoryId,
            @RequestParam MultipartFile image){
        return categoryService.uploadCategoryImage(categoryId, image);
    }
}
