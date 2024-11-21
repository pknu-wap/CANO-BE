package com.wap.cano_be.service.impl;

import com.wap.cano_be.domain.Like;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.repository.LikeRepository;
import com.wap.cano_be.repository.MemberRepository;
import com.wap.cano_be.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

//    @Transactional
//    public void updateLike(long memberId, long menuId, boolean like){
//        Member member = memberRepository.findBySocialId(memberId).orElseThrow(()->new IllegalArgumentException("Member not found"));
//        Menu menu = menuRepository.findById(menuId);
//
//        // 좋아요 했다면
//        if(like){
//            // member - menu 간 join이 없다면
//            if(!likeRepository.existsByMemberAndMenu(member, menu)){
//                menu.increaseLikeCount();
//                menuRepository.save(menu); // transactional 고려해 이후 제거할 수도 있음
//                likeRepository.save(new Like(member, menu));
//            }
//        } else {
//            // 좋아요 해제
//            menu.decreaseLikeCount();
//            menuRepository.save(menu);
//            likeRepository.deleteByMemberAndMenu(member, menu);
//        }
//    }
}
