package com.qcommerce.backend.dto.response;

public record ItemResponse(
        String itemId,
        String itemName,
        String itemDescription,
        double itemPrice,
        String itemImage,
        int availableQuantity,
        boolean active,
        String categoryId,
        String categoryName
) {
}
