package com.qcommerce.backend.service;

import com.qcommerce.backend.dto.response.ItemResponse;
import com.qcommerce.backend.dto.response.PageResponse;
import com.qcommerce.backend.entity.Item;
import com.qcommerce.backend.mapper.ItemMapper;
import com.qcommerce.backend.mapper.PageMapper;
import com.qcommerce.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public PageResponse<ItemResponse> getItems(String search, String categoryId, Double minPrice, Double maxPrice,
                                               Boolean active, int pageSize, int pageNumber, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                sortDir.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Direction.ASC, sortBy)
                        : Sort.by(Sort.Direction.DESC, sortBy));

        Page<Item> pageItem = itemRepository.searchItems(search, categoryId, minPrice, maxPrice, active, pageable);
        Page<ItemResponse> pageItemResponse = new PageImpl<ItemResponse>(pageItem.stream().map(ItemMapper::toResponse).toList()) ;
        return PageMapper.toResponse(pageItemResponse);
    }
}
