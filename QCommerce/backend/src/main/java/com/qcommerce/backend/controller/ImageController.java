package com.qcommerce.backend.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qcommerce.backend.dto.response.CategoryResponse;
import com.qcommerce.backend.dto.response.ItemResponse;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.service.ImageService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // ── Category image ────────────────────────────────────────────────────────

    @PostMapping("/categories/{categoryId}")
    public CategoryResponse uploadCategoryImage(
            @PathVariable String categoryId,
            @RequestParam MultipartFile image) {

        return imageService.uploadCategoryImage(categoryId, image);
    }

    @GetMapping(value = "/categories/{categoryId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getCategoryImage(@PathVariable String categoryId) throws IOException {

        byte[] imageBytes = imageService.getCategoryImage(categoryId);
        return imageBytes;
    }

    // ── Item image ────────────────────────────────────────────────────────────

    @PostMapping("/items/{itemId}")
    public ItemResponse uploadItemImage(@PathVariable String itemId,
                                        @RequestParam MultipartFile image) {

        return imageService.uploadItemImage(itemId, image);
    }

    @GetMapping(value = "/items/{itemId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getItemImage(@PathVariable String itemId) throws IOException {

        byte[] imageBytes = imageService.getItemImage(itemId);
        return imageBytes;
    }

    // ── Profile image ─────────────────────────────────────────────────────────

    @PostMapping("/users/profile")
    public UserResponse uploadProfileImage(@RequestAttribute String userId,
                                           @RequestParam MultipartFile image) {

        return imageService.uploadProfileImage(userId, image);
    }

    @GetMapping(value = "/users/{userId}", produces =  {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getProfileImage(@PathVariable String userId,
                                  HttpServletResponse response) throws IOException {

        byte[] imageBytes = imageService.getProfileImage(userId);
        return imageBytes;
    }

    @DeleteMapping("/users/profile")
    public UserResponse removeProfileImage(@RequestAttribute String userId) {
        return imageService.removeProfileImage(userId);
    }
}