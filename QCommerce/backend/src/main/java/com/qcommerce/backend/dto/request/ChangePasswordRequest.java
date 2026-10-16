package com.qcommerce.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(

        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",
                message = "New password must be 8-20 characters and include at least one uppercase letter, one lowercase letter, one digit, and one special character"
        )
        String newPassword,

        @NotBlank(message = "Please confirm your new password")
        String confirmPassword
) {
}
