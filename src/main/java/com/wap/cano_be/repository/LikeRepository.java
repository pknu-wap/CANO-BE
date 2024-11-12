package com.wap.cano_be.repository;

import com.wap.cano_be.domain.Like;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberAndMenu(Member member, Menu menu);
    void deleteByMemberAndMenu(Member member, Menu menu);
}
