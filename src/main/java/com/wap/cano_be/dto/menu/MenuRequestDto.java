package com.wap.cano_be.dto.menu;

public record MenuRequestDto(
        String cafeName,
        String name,
        int price,
        String imageUrl
) {
}
