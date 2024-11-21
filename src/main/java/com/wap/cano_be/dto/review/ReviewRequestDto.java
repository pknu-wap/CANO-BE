package com.wap.cano_be.dto.review;

import com.wap.cano_be.domain.enums.Degree;

public record ReviewRequestDto(
        Double score,
        String contents,
        Degree acidity,
        Degree body,
        Degree bitterness,
        Degree sweetness
) {
}
