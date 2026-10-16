package com.qcommerce.backend.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        String fullName,

        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        String phoneNumber,

        String address,
        String city,
        String state,
        String zipCode
        ) {
}
