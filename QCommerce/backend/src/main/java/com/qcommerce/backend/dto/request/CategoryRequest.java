package com.qcommerce.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @Size(min = 3, max = 100, message = "Category name must be between 3 and 100 characters")
        @NotBlank(message = "Category name should not be blank")
        String categoryName
) {
}
