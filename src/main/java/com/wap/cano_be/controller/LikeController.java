package com.wap.cano_be.controller;

import com.wap.cano_be.domain.PrincipalDetail;
import com.wap.cano_be.service.impl.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 좋아요 등록
    @PostMapping("/menus/{menu_id}/like")
    public ResponseEntity<?> like(
            @PathVariable("menu_id") long menuId,
            @AuthenticationPrincipal PrincipalDetail principalDetail){
        return likeService.insert(principalDetail.getMember().getId(), menuId);
    }

    // [TEST] 좋아요 한 메뉴 조회
    @GetMapping("/likes/me")
    public ResponseEntity<?> getLikes(
            @AuthenticationPrincipal PrincipalDetail principalDetail
    ) {
        return likeService.getLikedMenus(principalDetail.getMember().getId());
    }

    // 좋아요 취소
    @DeleteMapping("/menus/{menu_id}/like")
    public ResponseEntity<?> unlike(
            @PathVariable("menu_id") long menuId,
            @AuthenticationPrincipal PrincipalDetail principalDetail){
        return likeService.delete(principalDetail.getMember().getId(), menuId);
    }
}
