package com.qcommerce.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.qcommerce.backend.dto.response.OrderItemResponse;
import com.qcommerce.backend.dto.response.OrderResponse;
import com.qcommerce.backend.entity.OrderEntity;
import com.qcommerce.backend.entity.OrderItem;

public final class OrderMapper {

    private OrderMapper() {}

    public static OrderResponse toResponse(OrderEntity order) {
        if (order == null) return null;

        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(OrderMapper::toItemResponse)
                .collect(Collectors.toList());

        String userName = (order.getUser() != null && order.getUser().getContactInfo() != null)
                ? order.getUser().getContactInfo().getFullName()
                : null;

        return new OrderResponse(order.getOrderId(), order.getUser().getUserId(),
                order.getUser().getUserEmail(), userName, items,
                order.getTotalAmount(), order.getDeliveryAddress(),
                order.getPhoneNumber(), order.getStatus().name(),
                order.getCreatedAt());
    }

    public static OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(item.getOrderItemId(), item.getItemName(),
                item.getItemPrice(), item.getItemImage(),
                item.getQuantity(), item.getItemPrice() * item.getQuantity());
    }
}