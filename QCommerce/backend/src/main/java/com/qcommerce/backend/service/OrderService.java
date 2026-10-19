package com.qcommerce.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.qcommerce.backend.dto.request.CheckoutRequest;
import com.qcommerce.backend.dto.request.UpdateOrderStatusRequest;
import com.qcommerce.backend.dto.response.OrderResponse;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.entity.Cart;
import com.qcommerce.backend.entity.CartItem;
import com.qcommerce.backend.entity.Item;
import com.qcommerce.backend.entity.OrderEntity;
import com.qcommerce.backend.entity.OrderItem;
import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.entity.helper.OrderStatus;
import com.qcommerce.backend.exception.CartEmptyException;
import com.qcommerce.backend.exception.ForbiddenException;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.OrderMapper;
import com.qcommerce.backend.mapper.PageMapper;
import com.qcommerce.backend.repository.CartRepository;
import com.qcommerce.backend.repository.ItemRepository;
import com.qcommerce.backend.repository.OrderRepository;
import com.qcommerce.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse checkout(String userId, CheckoutRequest request) {
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CartEmptyException("Cart is empty"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is empty. Add items before checkout.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // ── Deduct stock for every cart item
        // Validate active status AND sufficient stock before touching anything.
        // Issue 3 fix: reject inactive items at checkout, not just at add-to-cart.
        for (CartItem ci : cart.getItems()) {
            Item item = ci.getItem();

            if (!item.isActive()) {
                throw new CartEmptyException("""
                    "%s" is no longer available. Please remove it from your cart before placing the order.
                    """.formatted(item.getItemName()));

            }

            if (item.getAvailableQuantity() < ci.getQuantity()) {
                throw new CartEmptyException("""
                    Insufficient stock for "%s". Available: %d, requested: %d.\
                    """.formatted(item.getItemName(), item.getAvailableQuantity(), ci.getQuantity()));

            }
        }

        // All validations passed — now atomically decrement stock
        for (CartItem ci : cart.getItems()) {
            Item item = ci.getItem();
            itemRepository.decrementStock(item.getItemId(), ci.getQuantity());
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(ci -> OrderItem.builder()
                        .item(ci.getItem())
                        .itemName(ci.getItem().getItemName())
                        .itemPrice(ci.getItem().getItemPrice())
                        .itemImage(ci.getItem().getItemImage())
                        .quantity(ci.getQuantity())
                        .build()
                ).collect(Collectors.toList());

        double totalAmount = orderItems.stream()
                .mapToDouble(oi -> oi.getItemPrice() * oi.getQuantity())
                .sum();

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .orderItems(orderItems)
                .totalAmount(totalAmount)
                .deliveryAddress(request.deliveryAddress())
                .phoneNumber(request.phoneNumber())
                .status(OrderStatus.PENDING)
                .build();

        // Link order items back to the order
        orderItems.forEach(oi -> oi.setOrder(order));

        OrderEntity saved = orderRepository.save(order);

        // Clear cart after successful order
        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderMapper.toResponse(saved);
    }

    public PageResponse<OrderResponse> getUserOrders(String userId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<OrderEntity> ordersPage = orderRepository.findByUserUserId(userId, pageable);
        Page<OrderResponse> ordersResponsePage = ordersPage.map(OrderMapper::toResponse);
        return PageMapper.toResponse(ordersResponsePage);
    }

    public OrderResponse getOrderById(String orderId, String userId) {
        OrderEntity order = findOrderOrThrow(orderId);
        return OrderMapper.toResponse(order);
    }

    /**
     * Allows users to cancel their own PENDING orders.
     * Once the order moves to PREPARING — cancellation is no longer allowed from the user side.
     */
    @Transactional
    public OrderResponse cancelOrder(String orderId, String userId) {
        OrderEntity order = findOrderOrThrow(orderId);

        if (!order.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("You can only cancel your own orders");
        }

        // Only PENDING orders can be cancelled by the user
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException(
                    "Only orders in PENDING status can be cancelled. " +
                            "Current status: " + order.getStatus()
            );
        }

        // Restore stock before changing status
        restoreStockForOrder(order);

        order.setStatus(OrderStatus.CANCELLED);
        OrderEntity updated = orderRepository.save(order);

        return OrderMapper.toResponse(updated);
    }

    public PageResponse<OrderResponse> getAllOrders(
            String search, String status, int pageNumber, int pageSize, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        OrderStatus orderStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            } catch (RuntimeException e) {
                throw new ResourceNotFoundException("Invalid order status: " + status);
            }
        }

        Page<OrderEntity> ordersPage = orderRepository.searchOrders(search, orderStatus, pageable);
        Page<OrderResponse> ordersResponsePage = ordersPage.map(OrderMapper::toResponse);
        return PageMapper.toResponse(ordersResponsePage);
    }

    @Transactional
    public OrderResponse updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        OrderEntity order = findOrderOrThrow(orderId);

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.status().toUpperCase());
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Invalid status: " + request.status());
        }

        validateStatusTransition(order.getStatus(), newStatus);

        // ── Restore stock when order is cancelled
        if (newStatus == OrderStatus.CANCELLED) {
            restoreStockForOrder(order);
        }

        order.setStatus(newStatus);
        OrderEntity updated = orderRepository.save(order);
        return OrderMapper.toResponse(updated);
    }

    private void restoreStockForOrder(OrderEntity order) {
        for (OrderItem oi : order.getOrderItems()) {
            if (oi.getItem() != null) {
                itemRepository.restoreStock(oi.getItem().getItemId(), oi.getQuantity());
            }
        }
    }

    private OrderEntity findOrderOrThrow(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }

    /**
     * Validates sequential status transition.
     * PENDING → PREPARING → OUT_FOR_DELIVERY → DELIVERED
     * Any → CANCELLED
     */
    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (next == OrderStatus.CANCELLED) return;

        if (current == OrderStatus.CANCELLED || current == OrderStatus.DELIVERED) {
            throw new RuntimeException(
                    "Cannot transition from a status: " + current);
        }

        List<OrderStatus> sequence = List.of(
                OrderStatus.PENDING,
                OrderStatus.PREPARING,
                OrderStatus.OUT_FOR_DELIVERY,
                OrderStatus.DELIVERED
        );

        int currentIdx = sequence.indexOf(current);
        int nextIdx    = sequence.indexOf(next);

        if (nextIdx != currentIdx + 1) {
            throw new RuntimeException(
                    "Invalid status transition from " + current + " to " + next);
        }
    }
}
