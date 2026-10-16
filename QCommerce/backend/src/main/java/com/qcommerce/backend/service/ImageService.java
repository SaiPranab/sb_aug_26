package com.qcommerce.backend.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.dto.response.CategoryResponse;
import com.qcommerce.backend.dto.response.ItemResponse;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.entity.Category;
import com.qcommerce.backend.entity.Item;
import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.CategoryMapper;
import com.qcommerce.backend.mapper.ItemMapper;
import com.qcommerce.backend.mapper.UserMapper;
import com.qcommerce.backend.repository.CategoryRepository;
import com.qcommerce.backend.repository.ItemRepository;
import com.qcommerce.backend.repository.UserRepository;
import com.qcommerce.backend.util.FileStorageUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
private final CategoryRepository categoryRepository;
private final ItemRepository     itemRepository;
private final UserRepository     userRepository;

// ── Category image ────────────────────────────────────────────────────────

public CategoryResponse uploadCategoryImage(String categoryId, MultipartFile image) {
    Category category = findCategoryOrThrow(categoryId);

    // Replace old image if exists
    if (category.getCategoryImage() != null) {
        FileStorageUtil.deleteFile(category.getCategoryImage(), AppConstants.UPLOAD_DIR_CATEGORIES);
    }

    String newFileName = FileStorageUtil.saveImage(image, AppConstants.UPLOAD_DIR_CATEGORIES);
    category.setCategoryImage(newFileName);
    categoryRepository.save(category);

    return CategoryMapper.toResponse(category);
}

public byte[] getCategoryImage(String categoryId) throws IOException {
    Category category = findCategoryOrThrow(categoryId);

    String imageName = category.getCategoryImage();
    if (imageName == null || imageName.isBlank()) {
        throw new ResourceNotFoundException("No image found for this category");
    }

    return readFileBytes(AppConstants.UPLOAD_DIR_CATEGORIES, imageName);
}

// ── Item image ────────────────────────────────────────────────────────────

public ItemResponse uploadItemImage(String itemId, MultipartFile image) {
    Item item = findItemOrThrow(itemId);

    if (item.getItemImage() != null) {
        FileStorageUtil.deleteFile(item.getItemImage(), AppConstants.UPLOAD_DIR_ITEMS);
    }

    String newFileName = FileStorageUtil.saveImage(image, AppConstants.UPLOAD_DIR_ITEMS);
    item.setItemImage(newFileName);
    itemRepository.save(item);

    return ItemMapper.toResponse(item);
}

public byte[] getItemImage(String itemId) throws IOException {
    Item item = findItemOrThrow(itemId);

    String imageName = item.getItemImage();
    if (imageName == null || imageName.isBlank()) {
        throw new ResourceNotFoundException("No image found for this item");
    }

    return readFileBytes(AppConstants.UPLOAD_DIR_ITEMS, imageName);
}

// ── Profile image ─────────────────────────────────────────────────────────

public UserResponse uploadProfileImage(String userId, MultipartFile image) {
    UserEntity user = findUserOrThrow(userId);

    if (user.getProfileImage() != null) {
        FileStorageUtil.deleteFile(user.getProfileImage(), AppConstants.UPLOAD_DIR_PROFILE_IMAGE);
    }

    String newFileName = FileStorageUtil.saveImage(image, AppConstants.UPLOAD_DIR_PROFILE_IMAGE);
    user.setProfileImage(newFileName);
    userRepository.save(user);

    return UserMapper.toResponse(user);
}

public byte[] getProfileImage(String userId) throws IOException {
    UserEntity user = findUserOrThrow(userId);

    String imageName = user.getProfileImage();
    if (imageName == null || imageName.isBlank()) {
        throw new ResourceNotFoundException("No profile image found for this user");
    }

    return readFileBytes(AppConstants.UPLOAD_DIR_PROFILE_IMAGE, imageName);
}

public UserResponse removeProfileImage(String userId) {
    UserEntity user = findUserOrThrow(userId);

    if (user.getProfileImage() == null) {
        throw new ResourceNotFoundException("No profile image to remove");
    }

    FileStorageUtil.deleteFile(user.getProfileImage(), AppConstants.UPLOAD_DIR_PROFILE_IMAGE);
    user.setProfileImage(null);
    userRepository.save(user);

    return UserMapper.toResponse(user);
}

// ── Shared helpers ────────────────────────────────────────────────────────

/**
 * Reads a file and returns its raw bytes.
 */
private byte[] readFileBytes(String directory, String fileName) throws IOException {
    File file = new File(directory + File.separator + fileName);
    if (!file.exists()) {
        throw new ResourceNotFoundException("Image file not found on server: " + fileName);
    }
    try (FileInputStream fis = new FileInputStream(file)) {
        return fis.readAllBytes();
    }
}

// ── Image name resolvers (used by controller for Content-Type) ───────────

public String getCategoryImageName(String categoryId) {
    return findCategoryOrThrow(categoryId).getCategoryImage();
}

public String getItemImageName(String itemId) {
    return findItemOrThrow(itemId).getItemImage();
}

public String getProfileImageName(String userId) {
    return findUserOrThrow(userId).getProfileImage();
}

private Category findCategoryOrThrow(String id) {
    return categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
}

private Item findItemOrThrow(String id) {
    return itemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
}

private UserEntity findUserOrThrow(String id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
}
}
