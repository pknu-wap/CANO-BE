package com.wap.cano_be.service.impl;

import com.wap.cano_be.domain.Like;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.repository.LikeRepository;
import com.wap.cano_be.repository.MemberRepository;
import com.wap.cano_be.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, MemberRepository memberRepository, MenuRepository menuRepository) {
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.menuRepository = menuRepository;
    }

    public ResponseEntity<?> insert(long memberId, long menuId){
        Member member = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("Member not found"));
        Menu menu = menuRepository.findById(menuId);

        // 이미 좋아요라면 409 에러 반환
        if(likeRepository.existsByMemberAndMenu(member, menu)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // 좋아요 반영
        Like like = Like.builder()
                .member(member)
                .menu(menu)
                .build();
        likeRepository.save(like);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> delete(long memberId, long menuId){
        Member member = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("Member not found"));
        Menu menu = menuRepository.findById(menuId);
        
        // 좋아요가 있다면 좋아요 해제
        if(likeRepository.existsByMemberAndMenu(member, menu)){
            likeRepository.deleteByMemberAndMenu(member, menu);
            return ResponseEntity.noContent().build();
        }

        // 없으면 409 에러 반환
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // [TEST] 좋아요한 메뉴 찾기
    public ResponseEntity<List<Menu>> getLikedMenus(long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("Member not found"));
        return ResponseEntity.ok().body(
                member.getLikes().stream()
                .map(Like::getMenu)
                .collect(Collectors.toList()));
    }

}
