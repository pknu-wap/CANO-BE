package com.wap.cano_be.dto.review;

import com.wap.cano_be.domain.Review;
import com.wap.cano_be.domain.ReviewImage;
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
                Degree.fromPercentage(review.getAcidity()),
                Degree.fromPercentage(review.getBody()),
                Degree.fromPercentage(review.getBitterness()),
                Degree.fromPercentage(review.getSweetness()),
                review.getMember().getId(),
                review.getMenu().getId(),
                review.getImages().stream().map(ReviewImage::getUrl).toList(),
                review.getCreatedAt()
        );
    }
}