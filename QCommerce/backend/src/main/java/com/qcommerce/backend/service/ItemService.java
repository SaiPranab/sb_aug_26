package com.qcommerce.backend.service;

import com.qcommerce.backend.dto.request.ItemRequest;
import com.qcommerce.backend.dto.response.ItemResponse;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.entity.Category;
import com.qcommerce.backend.entity.Item;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.ItemMapper;
import com.qcommerce.backend.mapper.PageMapper;
import com.qcommerce.backend.repository.CategoryRepository;
import com.qcommerce.backend.repository.ItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public PageResponse<ItemResponse> getItems(String search, String categoryId, Double minPrice, Double maxPrice,
                                               Boolean active, int pageSize, int pageNumber, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                sortDir.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Direction.ASC, sortBy)
                        : Sort.by(Sort.Direction.DESC, sortBy));

        Page<Item> pageItem = itemRepository.searchItems(search, categoryId, minPrice, maxPrice, active, pageable);
        Page<ItemResponse> pageItemResponse = pageItem.map(ItemMapper::toResponse);
        return PageMapper.toResponse(pageItemResponse);
    }

    public ItemResponse getItemById(String itemId) {
        Item itemById = findByIdOrThrow(itemId);
        return ItemMapper.toResponse(itemById);
    }

    public void deleteItemById(String itemId) {
        Item itemById = findByIdOrThrow(itemId);
        itemRepository.delete(itemById);
    }

    public ItemResponse createItem(ItemRequest itemRequest) {
        Category categoryById = findByCategoryIdOrThrow(itemRequest.categoryId());
        Item newItem = ItemMapper.toEntity(itemRequest, categoryById);
        Item savedItem = itemRepository.save(newItem);
        return ItemMapper.toResponse(savedItem);
    }

    private Item findByIdOrThrow(String itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item Not Found"));
    }

    private Category findByCategoryIdOrThrow(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public ItemResponse updateItem(String itemId, ItemRequest itemRequest) {
        findByIdOrThrow(itemId);
        Category categoryById = findByCategoryIdOrThrow(itemRequest.categoryId());

        Item exisitngItem = ItemMapper.toEntity(itemRequest, categoryById);
        exisitngItem.setItemId(itemId);

        Item updatedItem = itemRepository.save(exisitngItem);
        return ItemMapper.toResponse(updatedItem);
    }
}
