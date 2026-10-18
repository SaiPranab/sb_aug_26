package com.qcommerce.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qcommerce.backend.dto.request.AddToCartRequest;
import com.qcommerce.backend.dto.response.CartResponse;
import com.qcommerce.backend.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(
            @RequestAttribute String userId) {

        return cartService.getCart(userId);
    }

    @PostMapping("/add")
    public CartResponse addToCart(
            @RequestAttribute String userId,
            @Valid @RequestBody AddToCartRequest request) {

        return cartService.addToCart(userId, request);
    }

    @PutMapping("/items/{cartItemId}")
    public CartResponse updateQuantity(
            @RequestAttribute String userId,
            @PathVariable String cartItemId,
            @RequestParam int quantity) {

        return cartService.updateQuantity(
                userId, cartItemId, quantity);
    }

    @DeleteMapping("/items/{cartItemId}")
    public CartResponse removeItem(
            @RequestAttribute String userId,
            @PathVariable String cartItemId) {

        return cartService.removeItem(userId, cartItemId);
    }

    @DeleteMapping("/clear")
    public void clearCart(
            @RequestAttribute String userId) {

        cartService.clearCart(userId);
    }
}