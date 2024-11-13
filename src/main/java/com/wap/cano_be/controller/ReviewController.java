package com.wap.cano_be.controller;

import com.wap.cano_be.domain.PrincipalDetail;
import com.wap.cano_be.dto.review.ReviewRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        //....
        return ResponseEntity.ok(reviewRequestDto);
    }

    @GetMapping("/{review_id}")
    public ResponseEntity<?> getReviewById(@PathVariable("review_id") long reviewId) {
        //...
        return ResponseEntity.ok(reviewId);
    }

    @PatchMapping("/{review_id}")
    public ResponseEntity<?> updateReview(
            @RequestBody ReviewRequestDto reviewRequestDto,
            @PathVariable("review_id") long reviewId,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        //....
        return ResponseEntity.ok(reviewRequestDto);
    }


    @DeleteMapping("/{review_id}")
    public ResponseEntity<?> deleteReview(
            @PathVariable("review_id") long reviewId,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        // ....
        return ResponseEntity.ok(reviewId);
    }
}
