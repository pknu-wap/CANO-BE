package com.wap.cano_be.dto.review;

import java.util.List;

public record ReviewRequestDto(
        double score,
        String contents,
        List<String> imageUrls,
        String acidity,
        String body,
        String bitterness,
        String sweetness,
        List<String> aromas
) {
}
