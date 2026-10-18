package com.qcommerce.backend.dto.response;

public record CartItemResponse(
        String cartItemId,
        String itemId,
        String itemName,
        String itemImage,
        double itemPrice,
        int quantity,
        double subtotal,
        int availableQuantity) {
}
