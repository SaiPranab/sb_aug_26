package com.qcommerce.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.dto.request.CheckoutRequest;
import com.qcommerce.backend.dto.request.UpdateOrderStatusRequest;
import com.qcommerce.backend.dto.response.OrderResponse;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@RequestAttribute String userId,
                                  @Valid @RequestBody CheckoutRequest request) {

        return orderService.checkout(userId, request);
    }

    @GetMapping("/orders/my")
    public PageResponse<OrderResponse> getMyOrders(@RequestAttribute String userId,
                                                   @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
                                                   @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize) {

        return orderService.getUserOrders(userId, pageNumber, pageSize);
    }

    @GetMapping("/orders/{orderId}")
    public OrderResponse getOrderById(@RequestAttribute String userId,
                                      @PathVariable String orderId) {

        return orderService.getOrderById(orderId, userId);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    public OrderResponse cancelOrder(@RequestAttribute String userId,
                                     @PathVariable String orderId) {

        return orderService.cancelOrder(orderId, userId);
    }

    // ── Admin endpoints ────────────────────────────────────────────

    @GetMapping("/admin/orders")
    public PageResponse<OrderResponse> getAllOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = "desc") String sortDir) {

        return orderService.getAllOrders(
                search, status, pageNumber, pageSize, sortDir);
    }

    @PutMapping("/admin/orders/{orderId}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable String orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        return orderService.updateOrderStatus(orderId, request);
    }
}