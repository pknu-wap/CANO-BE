package com.wap.cano_be.repository;

import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.Menu;
import com.wap.cano_be.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMenu(Menu menu);
    void deleteByMemberAndMenu(Member member, Menu menu);
}
