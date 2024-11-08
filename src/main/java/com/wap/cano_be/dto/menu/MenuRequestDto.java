package com.wap.cano_be.dto.menu;

import java.util.List;

public record MenuRequestDto(
        String name,
        int price,
        String acidity,
        String body,
        String bitterness,
        String sweetness,
        List<String> aromas
) {
}
