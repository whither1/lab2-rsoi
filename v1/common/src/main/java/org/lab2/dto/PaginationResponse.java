package org.lab2.dto;

import java.util.List;

public record PaginationResponse<T>(
        int page,
        int pageSize,
        long totalElements,
        List<T> items
) {
}
