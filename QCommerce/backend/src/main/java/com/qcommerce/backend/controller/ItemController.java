package com.qcommerce.backend.controller;

import com.qcommerce.backend.constants.AppConstants;
import com.qcommerce.backend.dto.request.ItemRequest;
import com.qcommerce.backend.dto.response.ItemResponse;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public PageResponse<ItemResponse> getItems(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "true") Boolean active,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = "itemName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        System.out.println("Controller Called");
        return itemService.getItems(search, categoryId, minPrice, maxPrice, active, pageSize, pageNumber, sortBy, sortDir);
    }

    @GetMapping("/{itemId}")
    public ItemResponse getItem(@PathVariable String itemId) {
        return itemService.getItemById(itemId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String itemId) {
        itemService.deleteItemById(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse createItem(@RequestBody @Valid ItemRequest itemRequest) {
        return itemService.createItem(itemRequest);
    }

    @PutMapping("/{itemId}")
    public ItemResponse updateItem(@PathVariable String itemId,
                                   @RequestBody @Valid ItemRequest itemRequest) {
        return itemService.updateItem(itemId, itemRequest);
    }
}
