package com.wap.cano_be.dto.menu;

import lombok.Builder;

import java.util.Map;

@Builder
public record MenuAromasResponseDto(
        String name,
        double score,
        String imageUrl,
        Map<String, Integer> aromas
) {
}
