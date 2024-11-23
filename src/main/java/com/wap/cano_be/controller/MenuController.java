package com.wap.cano_be.controller;

import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.PrincipalDetail;
import com.wap.cano_be.dto.menu.*;
import com.wap.cano_be.service.impl.LikeService;
import com.wap.cano_be.service.impl.MenuService;
import com.wap.cano_be.service.impl.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuService menuService;
    private final LikeService likeService;
    private final ReviewService reviewService;

    @Autowired
    public MenuController(MenuService menuService, LikeService likeService, ReviewService reviewService) {
        this.menuService = menuService;
        this.likeService = likeService;
        this.reviewService = reviewService;
    }

    private ResponseEntity<Map<String, String>> getNoDataResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("error","조건에 맞는 데이터가 존재하지 않습니다.");
        return ResponseEntity.badRequest().body(response);
    }

    // 메뉴 조회
//    @GetMapping("/attribute")
//    public ResponseEntity<?> getMenusByAttribute(
//            @RequestParam String type,
//            @RequestParam String degree
//            ) {
//        List<MenuAttributeResponseDto> menus = menuService.getMenuByAttribute(type, degree);
//        if(menus == null || menus.isEmpty()){
//            return getNoDataResponse();
//        }
//        return ResponseEntity.ok().body(menus);
//    }

    // 메뉴 조회 - 아로마
//    @GetMapping("/search/aroma")
//    public ResponseEntity<?> getMenusByAromas(
//            @RequestParam List<String> aromas
//    ){
//        List<MenuAromasResponseDto> responseDtos = menuService.getMenuByAromas(aromas);
//        if(responseDtos.isEmpty()){
//            return getNoDataResponse();
//        }
//        return ResponseEntity.ok().body(responseDtos);
//    }

    // 메뉴 검색
    @GetMapping("/search")
    public ResponseEntity<List<MenuResponseDto>> getMenuByKeyword(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String attribute,
            @RequestParam(required = false) String degree
    ){
        return menuService.searchMenus(query, attribute, degree);
    }
//
//    // 메뉴 데이터 조회
//    @GetMapping("/{menu_id}")
//    public ResponseEntity<?> getMenuInfo(@PathVariable("menu_id") long id){
//        MenuResponseDto menuResponseDto = menuService.getMenuInfo(id);
//        if(menuResponseDto == null){
//            return getNoDataResponse();
//        }
//        return ResponseEntity.ok().body(menuResponseDto);
//    }

    private ResponseEntity<Map<String, String>> getSuccessResponse() {
        Map<String, String> response = new HashMap<>();
        response.put("success", "정상 처리 되었습니다");
        return ResponseEntity.ok().body(response);
    }

    // 메뉴 등록
    @PostMapping
    public ResponseEntity<MenuResponseDto> createMenu(
            @RequestPart(value = "dto") MenuRequestDto requestDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ){
        log.info("========POST MENU========");
        log.info("menuRequestDto: {}", requestDto);
        return menuService.createMenu(requestDto, image);
    }

    // 메뉴 조회
    @GetMapping("/{menu_id}")
    public ResponseEntity<MenuResponseDto> getMenuById(@PathVariable("menu_id") long menuId) {
        return menuService.getMenuById(menuId);
    }

    // 리뷰 등록
//    @PostMapping("/{menu_id}/review")
//    public ResponseEntity<?> writeReview(
//            @PathVariable("menu_id") long id,
//            @RequestBody ReviewRequestDto reviewRequestDto,
//            @AuthenticationPrincipal PrincipalDetail principalDetail){
//        long memberId = principalDetail.getMember().getSocialId();
//        reviewService.createReview(reviewRequestDto, id, memberId);
//
//        return getSuccessResponse();
//    }

    // 메뉴 리포트
    @PostMapping("/report")
    public ResponseEntity<?> reportMenu(@RequestBody MenuReportDto menuReportDto){
        // DTO 를 서비스로 전달
        // 정상 처리 시 200 응답
        // 기타 에러 4XX 응답
        log.info("========POST REPORT========");
        log.info("menuReportDto: {}", menuReportDto);
        return getSuccessResponse();
    }

    // 좋아요
//    @PostMapping("/{menu_id}/like")
//    public ResponseEntity<?> setLike(
//            @PathVariable("menu_id") long id,
//            @RequestBody MenuLikeDto menuLikeDto,
//            @AuthenticationPrincipal PrincipalDetail principalDetail){
//
//        if(menuLikeDto == null){
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "잘못된 요청 양식 입니다.");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        Long memberId = principalDetail.getMember().getId();
//        if(memberId == null){
//            Map<String, String> response = new HashMap<>();
//            response.put("error", "유효하지 않은 사용자입니다.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//
//        likeService.updateLike(memberId, id, menuLikeDto.like());
//        return getSuccessResponse();
//    }
}
