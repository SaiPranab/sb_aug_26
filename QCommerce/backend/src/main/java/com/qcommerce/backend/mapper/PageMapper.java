package com.qcommerce.backend.mapper;

import com.qcommerce.backend.dto.response.PageResponse;
import org.springframework.data.domain.Page;

public class PageMapper {
    private PageMapper() {}

    public static PageResponse toResponse(Page page) {
        PageResponse response = new PageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );

        return response;
    }
}
