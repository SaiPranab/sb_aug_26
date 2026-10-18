package com.qcommerce.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.qcommerce.backend.dto.response.CartItemResponse;
import com.qcommerce.backend.dto.response.CartResponse;
import com.qcommerce.backend.entity.Cart;
import com.qcommerce.backend.entity.CartItem;

public final class CartMapper {
    private CartMapper() {}

    public static CartResponse toResponse(Cart cart) {
        List<CartItemResponse> itemResponses = (cart.getItems() == null
                ? List.<CartItem>of()
                : cart.getItems()).stream()
                .map(CartMapper::toItemResponse)
                .collect(Collectors.toList());

        double totalPrice = itemResponses.stream()
                .mapToDouble(CartItemResponse::subtotal)
                .sum();

        int totalQuantity = itemResponses.stream()
                .mapToInt(CartItemResponse::quantity)
                .sum();

        return new CartResponse(cart.getCartId(), itemResponses, totalPrice, totalQuantity);
    }

    public static CartItemResponse toItemResponse(CartItem cartItem) {
        return new CartItemResponse(cartItem.getCartItemId(), cartItem.getItem().getItemId(),
                cartItem.getItem().getItemName(), cartItem.getItem().getItemImage(),
                cartItem.getItem().getItemPrice(), cartItem.getQuantity(),
                cartItem.getItem().getItemPrice() * cartItem.getQuantity(),
                cartItem.getItem().getAvailableQuantity());
    }
}