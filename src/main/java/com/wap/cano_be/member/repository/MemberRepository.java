package com.wap.cano_be.member.repository;

import com.wap.cano_be.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findById(long id);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByEmail(String email);
}
