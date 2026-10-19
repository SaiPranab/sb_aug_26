package com.qcommerce.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(
        @NotBlank(message = "Delivery address is required")
        String deliveryAddress,

        @NotBlank(message = "Phone number is required")
        String phoneNumber
) {
}
