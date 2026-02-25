package com.example.adaprivelearningnavigator.service.dto.common;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> items,
        long total,
        int page,
        int size
) {}
