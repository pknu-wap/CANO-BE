package com.wap.cano_be.service.impl;

import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.Review;
import com.wap.cano_be.domain.ReviewImage;
import com.wap.cano_be.domain.enums.Degree;
import com.wap.cano_be.dto.review.ReviewRequestDto;
import com.wap.cano_be.dto.review.ReviewResponseDto;
import com.wap.cano_be.repository.MemberRepository;
import com.wap.cano_be.repository.MenuRepository;
import com.wap.cano_be.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    private double getDegree(String attribute){
        for (Degree deg:
             Degree.values()) {
            if(attribute.equalsIgnoreCase(deg.name())){
                return deg.getPercentage();
            }
        }
        return 0;
    }

    // 리뷰 생성
//    public void createReview(ReviewRequestDto reviewRequestDto, long menuId, long memberId) {
//        Menu menu = menuRepository.findById(menuId);
//        Member member = memberRepository.findBySocialId(memberId).orElseThrow(()->new IllegalArgumentException("member not found"));
//        if(menu == null){
//            return;
//        }
//
//        // String 값으로 들어온 Degree를 double 형으로 전환 (수치 계산을 위해)
//        double acidity = getDegree(reviewRequestDto.acidity());
//        double body = getDegree(reviewRequestDto.body());
//        double bitterness = getDegree(reviewRequestDto.bitterness());
//        double sweetness = getDegree(reviewRequestDto.sweetness());
//
//        // 리뷰 저장
//        Review review = Review.builder()
//                .member(member)
//                .menu(menu)
//                .score(reviewRequestDto.score())
//                .contents(reviewRequestDto.contents())
//                .acidity(acidity)
//                .body(body)
//                .bitterness(bitterness)
//                .sweetness(sweetness)
//                .aromas(reviewRequestDto.aromas())
//                .build();
//        reviewRepository.save(review);
//
//        // 평점과 각 속성 수치 반영
//        menu.calculateByReview(
//                reviewRequestDto.score(),
//                acidity,
//                body,
//                bitterness,
//                sweetness,
//                reviewRequestDto.aromas()
//        );
//
//        // 변경 사항 저장
//        menuRepository.save(menu);
//    }

    // 메뉴로부터 리뷰 조회
//    @ReadOnlyProperty
//    public List<ReviewResponseDto> getReviewsByMenuId(long menuId){
//        Menu menu = menuRepository.findById(menuId);
//
//        if(menu == null){
//            return null;
//        }
//        List<Review> reviews = reviewRepository.findAllByMenu(menu);
//        if(reviews == null || reviews.isEmpty()){
//            return null;
//        }
//
//        return reviews.stream()
//                .map(review -> ReviewResponseDto.builder()
//                        .memberName(review.getMember().getName())
//                        .writtenDate(review.getUpdatedAt() != null ?
//                                review.getUpdatedAt() : review.getCreatedAt())
//                        .contents(review.getContents())
//                        .imageUrls(review.getImageUrls())
//                        .build())
//                .collect(Collectors.toList());
//    }

    // 리뷰 삭제
    public ResponseEntity<?> deleteReview(long reviewId, long memberId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Review not found. reviewId: " + reviewId));

        // 리뷰 작성자가 아닌 경우 -> 400 Bad Request 반환
        if (review.getMember().getId() != memberId) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        // S3에서 이미지 삭제
        for (ReviewImage image : review.getImages()) {
            imageService.deleteImage(image.getUrl());
        }

        // 리뷰 삭제
        reviewRepository.delete(review);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<ReviewResponseDto> createReview(ReviewRequestDto requestDto, long menuId, long memberId, List<MultipartFile> images) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("멤버가 없습니다. memberId: " + memberId));
        Menu menu = menuRepository.findById(menuId);

        Review review = Review.builder()
                .contents(requestDto.contents())
                .score(requestDto.score())
                .member(member)
                .menu(menu)
                .build();

        try {
            if (requestDto.acidity().isPresent()) {
                review.setAcidity(requestDto.acidity().get().getPercentage());
            }
            if (requestDto.body().isPresent()) {
                review.setBody(requestDto.body().get().getPercentage());
            }
            if (requestDto.bitterness().isPresent()) {
                review.setBitterness(requestDto.bitterness().get().getPercentage());
            }
            if (requestDto.sweetness().isPresent()) {
                review.setSweetness(requestDto.sweetness().get().getPercentage());
            }

            // 이미지 업로드 및 URL 설정
            if (images != null && !images.isEmpty()) {
                List<String> imageUrls = imageService.uploadImages(images);
                List<ReviewImage> reviewImages = imageUrls.stream()
                        .map(url -> ReviewImage.builder()
                                .url(url)
                                .review(review)
                                .build())
                        .toList();
                review.setImages(reviewImages);
            }

            reviewRepository.save(review);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().body(new ReviewResponseDto(review));
    }

    public ResponseEntity<List<ReviewResponseDto>> getReviewsByMenuId(long menuId) {
        Menu menu = menuRepository.findById(menuId);
        if (menu == null) throw new IllegalArgumentException("Menu with id: " + menuId + " is not found.");
        List<Review> reviews = reviewRepository.findAllByMenu(menu);
        List<ReviewResponseDto> response = reviews.stream()
                .map(ReviewResponseDto::new)
                .toList();

        return ResponseEntity.ok().body(response);
    }
}
