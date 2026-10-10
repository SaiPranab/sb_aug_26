package com.qcommerce.backend.dto.request;

import jakarta.validation.constraints.*;

public record ItemRequest(
        @NotBlank(message = "Item name is required")
        @Size(min = 3, max = 150, message = "Item name should be between 3 and 150 characters")
        String itemName,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String itemDescription,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        Double itemPrice,

        @Min(value = 0, message = "Available Quantity cannot be negative")
        int availableQuantity,

        @NotNull(message = "Category is required")
        @NotBlank(message = "Category is required")
        String categoryId,

        Boolean active
) {
}
