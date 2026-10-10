package com.qcommerce.backend.dto.response;

import com.qcommerce.backend.entity.helper.Role;

public record UserResponse(
        String userId,
        String userEmail,
        Role userRole,
        String profileImage,
        boolean enabled,

        // ContactInfo
        String fullName,
        String phoneNumber,

        // AddressInfo
        String address,
        String city,
        String state,
        String zipCode
) {
}
