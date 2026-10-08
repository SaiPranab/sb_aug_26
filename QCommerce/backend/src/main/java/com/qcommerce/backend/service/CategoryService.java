package com.qcommerce.backend.service;

import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.dto.request.CategoryRequest;
import com.qcommerce.backend.dto.response.CategoryResponse;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.entity.Category;
import com.qcommerce.backend.exception.DuplicateEntryException;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.CategoryMapper;
import com.qcommerce.backend.mapper.PageMapper;
import com.qcommerce.backend.repository.CategoryRepository;
import com.qcommerce.backend.util.FileStorageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public PageResponse<Category> getAllCategories(String search, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest
                .of(pageNumber, pageSize, Sort.by("categoryName"));

        Page<Category> categoryPage = (search != null && !search.isBlank()) ?
                categoryRepository.findAllByCategoryNameContainingIgnoreCase(search, pageable) :
                categoryRepository.findAll(pageable);

        return PageMapper.toResponse(categoryPage);
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if(categoryRepository.existsByCategoryNameIgnoreCase(categoryRequest.categoryName())){
            throw new DuplicateEntryException("Category name already exists");
        }

        Category newCategory = Category.builder()
                .categoryName(categoryRequest.categoryName())
                .build();

        Category savedCategory = categoryRepository.save(newCategory);
        return CategoryMapper.toResponse(savedCategory);
    }

    public CategoryResponse getCategoryById(String categoryId) {
        Category category = findByIdOrThrow(categoryId);
        return CategoryMapper.toResponse(category);
    }

    public void deleteCategoryById(String categoryId) {
        findByIdOrThrow(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    public CategoryResponse updateCategoryById(String categoryId, CategoryRequest categoryRequest) {
        // category exists or not
        Category existingCategory = findByIdOrThrow(categoryId);

        // if existing category name matches the categoryRequest category name, then generate exception
        if(categoryRequest.categoryName().equals(existingCategory.getCategoryName())){
            throw new DuplicateEntryException("Category name already taken: " + existingCategory.getCategoryName());
        }

        // update the category name & save it in the DB
        existingCategory.setCategoryName(categoryRequest.categoryName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return CategoryMapper.toResponse(updatedCategory);
    }

    public CategoryResponse uploadCategoryImage(String categoryId, MultipartFile image) {
        // catgeory id exists or not
        Category existingCategory = findByIdOrThrow(categoryId);

        // if category image already exists then remove it
        if(existingCategory.getCategoryImage() != null) {
            // remove
        }

        // otherwise save the category image
        String newFileName = FileStorageUtil.saveImage(image, AppConstants.UPLOAD_DIR_CATEGORIES);
        existingCategory.setCategoryImage(newFileName);
        categoryRepository.save(existingCategory);

        return CategoryMapper.toResponse(existingCategory);
    }

    private Category findByIdOrThrow(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

}
