package com.wap.cano_be.dto.menu;

import com.wap.cano_be.domain.Menu;
import lombok.Builder;

import java.util.Map;
import java.util.OptionalDouble;

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
