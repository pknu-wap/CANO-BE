package com.wap.cano_be.member.repository;

import com.wap.cano_be.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByLoginId(String loginId);
}
