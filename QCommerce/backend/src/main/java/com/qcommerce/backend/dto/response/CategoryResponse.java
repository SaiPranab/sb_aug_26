package com.qcommerce.backend.dto.response;

import java.time.LocalDateTime;

public record CategoryResponse(
    String categoryId,
    String categoryName,
    String categoryImage,
    int itemCount
) {
}
