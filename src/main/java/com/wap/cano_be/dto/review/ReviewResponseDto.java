package com.wap.cano_be.dto.review;

import com.wap.cano_be.domain.Review;
import com.wap.cano_be.domain.enums.Degree;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewResponseDto(
        Long id,
        String memberName,
        String contents,
        Double score,
        Degree acidity,
        Degree body,
        Degree bitterness,
        Degree sweetness,
        Long memberId,
        Long menuId,
        List<String> imageUrls,
        LocalDateTime createdAt
) {
    public ReviewResponseDto(Review review) {
        this(
                review.getId(),
                review.getMember().getName(),
                review.getContents(),
                review.getScore(),
                review.getAcidity(),
                review.getBody(),
                review.getBitterness(),
                review.getSweetness(),
                review.getMember().getId(),
                review.getMenu().getId(),
                review.getImageUrls(),
                review.getCreatedAt()
        );
    }
}