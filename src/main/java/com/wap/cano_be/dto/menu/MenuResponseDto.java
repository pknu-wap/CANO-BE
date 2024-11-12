package com.wap.cano_be.dto.menu;

import lombok.Builder;

import java.util.List;

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
        List<String> aromas,
        String imageUrl
) {
}
