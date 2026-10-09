package com.qcommerce.backend.mapper;

import com.qcommerce.backend.dto.request.ItemRequest;
import com.qcommerce.backend.dto.response.ItemResponse;
import com.qcommerce.backend.entity.Category;
import com.qcommerce.backend.entity.Item;

public class ItemMapper {
    private ItemMapper() {}

    public static ItemResponse toResponse(Item item) {
        return new ItemResponse(
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
    }

    public static Item toEntity(ItemRequest itemRequest, Category category) {
        return Item.builder()
                .itemName(itemRequest.itemName())
                .itemDescription(itemRequest.itemDescription())
                .itemPrice(itemRequest.itemPrice())
                .availableQuantity(itemRequest.availableQuantity())
                .category(category)
                .active(true)
                .build();
    }
}
