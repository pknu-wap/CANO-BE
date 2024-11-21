package com.wap.cano_be.dto.review;

import com.wap.cano_be.domain.enums.Degree;

import java.util.Optional;

public record ReviewRequestDto(
        Double score,
        String contents,
        Optional<Degree> acidity,
        Optional<Degree> body,
        Optional<Degree> bitterness,
        Optional<Degree> sweetness
) {
}
