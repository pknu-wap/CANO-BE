package com.wap.cano_be.dto.menu;

import lombok.Builder;

@Builder
public record MenuAttributeResponseDto(
        long id,
        String name,
        double score,
        String attribute,
        double degree,
        String image_url
) {
}
