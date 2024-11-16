package com.wap.cano_be.dto.review;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewResponseDto(
        String memberName,
        LocalDateTime writtenDate,
        String contents,
        List<String> imageUrls
) {
}
