package com.qcommerce.backend.service;

import org.springframework.stereotype.Service;

import com.qcommerce.backend.dto.request.AddToCartRequest;
import com.qcommerce.backend.dto.response.CartResponse;
import com.qcommerce.backend.entity.Cart;
import com.qcommerce.backend.entity.CartItem;
import com.qcommerce.backend.entity.Item;
import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.exception.ForbiddenException;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.CartMapper;
import com.qcommerce.backend.repository.CartItemRepository;
import com.qcommerce.backend.repository.CartRepository;
import com.qcommerce.backend.repository.ItemRepository;
import com.qcommerce.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CartResponse getCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        return CartMapper.toResponse(cart);
    }

    @Transactional
    public CartResponse addToCart(String userId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId);
        Item item = findItemOrThrow(request.itemId());

        if (!item.isActive() || item.getAvailableQuantity() <= 0) {
            throw new IllegalStateException("Item is not available");
        }

        // Check if this item is already in the cart
        cart.getItems().stream()
                .filter(ci -> ci.getItem().getItemId().equals(request.itemId()))
                .findFirst()
                .ifPresentOrElse(
                        // --- existing item: update quantity
                        existing -> {
                            int newQty = Math.min(
                                    existing.getQuantity() + request.quantity(),
                                    item.getAvailableQuantity()
                            );
                            existing.setQuantity(newQty);

                        },
                        // --- no existing item: add a new one via collection
                        () -> {
                            int qty = Math.min(request.quantity(), item.getAvailableQuantity());
                            CartItem newItem = CartItem.builder()
                                    .cart(cart)
                                    .item(item)
                                    .quantity(qty)
                                    .build();

                            cart.getItems().add(newItem);
                        }
                );

        Cart saved = cartRepository.save(cart);
        return CartMapper.toResponse(saved);
    }

    @Transactional
    public CartResponse updateQuantity(String userId, String cartItemId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new ForbiddenException("Cart item does not belong to user");
        }

        if (quantity <= 0) {
            cart.getItems().removeIf(ci -> ci.getCartItemId().equals(cartItemId));
            cartRepository.save(cart);
        } else {
            int maxQty = cartItem.getItem().getAvailableQuantity();
            cartItem.setQuantity(Math.min(quantity, maxQty));
            cartItemRepository.save(cartItem);
        }

        return CartMapper.toResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(String userId, String cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new ForbiddenException("Cart item does not belong to user");
        }

        cart.getItems().removeIf(ci -> ci.getCartItemId().equals(cartItemId));
        cartRepository.save(cart);

        return CartMapper.toResponse(cart);
    }

    @Transactional
    public void clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(String userId) {
        return cartRepository.findByUser_UserId(userId).orElseGet(() -> {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

            Cart cart = Cart.builder()
                    .user(user)
                    .build();
            return cartRepository.save(cart);
        });
    }

    private Item findItemOrThrow(String itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));
    }
}
