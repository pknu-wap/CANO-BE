package com.wap.cano_be.controller;

import com.wap.cano_be.domain.PrincipalDetail;
import com.wap.cano_be.dto.review.ReviewRequestDto;
import com.wap.cano_be.dto.review.ReviewResponseDto;
import com.wap.cano_be.service.impl.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 메뉴로부터 리뷰 조회
//    @GetMapping("/menus/{menu_id}")
//    public ResponseEntity<?> getReviewById(@PathVariable("menu_id") long menuId) {
//        List<ReviewResponseDto> reviewResponseDtos = reviewService.getReviewsByMenuId(menuId);
//        if(reviewResponseDtos.isEmpty() || reviewResponseDtos == null){
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "잘못된 요청 양식입니다.");
//            return ResponseEntity.badRequest().body(response);
//        }
//        return ResponseEntity.ok().body(reviewResponseDtos);
//    }

    @PatchMapping("/{review_id}")
    public ResponseEntity<?> updateReview(
            @RequestBody ReviewRequestDto reviewRequestDto,
            @PathVariable("review_id") long reviewId,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        //....
        return ResponseEntity.ok(reviewRequestDto);
    }

    @PostMapping("/menus/{menu_id}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestBody ReviewRequestDto requestDto,
            @PathVariable("menu_id") long menuId,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return reviewService.createReview(requestDto, menuId, principalDetail.getMember().getId());
    }
}
