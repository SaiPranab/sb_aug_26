package com.qcommerce.backend.dto.response;

public record OrderItemResponse(
        String orderItemId,
        String itemName,
        double itemPrice,
        String itemImage,
        int quantity,
        double subtotal
) {
}
