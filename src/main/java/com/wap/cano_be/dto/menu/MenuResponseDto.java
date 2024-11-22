package com.wap.cano_be.dto.menu;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record MenuResponseDto(
        long id,
        String name,
        int price,
        Double score,
        Double acidity,
        Double body,
        Double bitterness,
        Double sweetness,
        Map<String, Integer> aromas,
        String imageUrl
) {
}
