package com.wap.cano_be.member.service;

import com.wap.cano_be.member.domain.Member;
import com.wap.cano_be.member.domain.MemberDTO;
import com.wap.cano_be.member.domain.MemberRole;
import com.wap.cano_be.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member saveMember(MemberDTO memberDTO) {
        Member member = Member.builder()
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .role(MemberRole.USER).build();
        return memberRepository.save(member);
    }

    public Map<String, String > findMyInfo(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();
        if(member.isEmpty()){
            response.put("Error", "user not found");
            return response;
        }

        response.put("name", member.get().getName());
        response.put("email", member.get().getEmail());
        response.put("socialId", member.get().getSocialId());
        response.put("profile", member.get().getProfileImageUrl());
        return response;
    }
}
