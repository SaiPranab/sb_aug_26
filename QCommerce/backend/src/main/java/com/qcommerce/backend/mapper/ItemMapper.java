package com.qcommerce.backend.mapper;

import com.qcommerce.backend.dto.response.ItemResponse;
import com.qcommerce.backend.entity.Item;

public class ItemMapper {
    private ItemMapper() {}

    public static ItemResponse toResponse(Item item) {
        ItemResponse response = new ItemResponse(
                item.getItemId(),
                item.getItemName(),
                item.getItemDescription(),
                item.getItemPrice(),
                item.getItemImage(),
                item.getAvailableQuantity(),
                item.isActive(),
                item.getCategory().getCategoryId(),
                item.getCategory().getCategoryName()
        );

        return response;
    }
}
