package com.wap.cano_be.dto.menu;

import java.util.List;

public record MenuRequestDto(
        String name,
        String acidity,
        String body,
        String bitterness,
        List<String> aroma
) {
}
