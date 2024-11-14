package com.wap.cano_be.dto.menu;

import lombok.Builder;

import java.util.Map;

@Builder
public record MenuResponseDto(
        long id,
        String name,
        int price,
        double score,
        double acidity,
        double body,
        double bitterness,
        double sweetness,
        Map<String, Integer> aromas,
        String imageUrl
) {
}
