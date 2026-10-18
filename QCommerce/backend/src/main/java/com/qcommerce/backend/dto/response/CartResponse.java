package com.qcommerce.backend.dto.response;

import java.util.List;

public record CartResponse(
        String cartId,
        List<CartItemResponse> items,
        double totalPrice,
        int totalQuantity
) {

}
