package com.qcommerce.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        String orderId,
        String userId,
        String userEmail,
        String userName,
        List<OrderItemResponse> orderItems,
        double totalAmount,
        String deliveryAddress,
        String phoneNumber,
        String status,
        LocalDateTime orderDate
) {
}

