package com.qcommerce.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @NotNull(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        @NotNull(message = "Password is required")
        String password
) {
}
