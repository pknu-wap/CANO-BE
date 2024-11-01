package com.wap.cano_be.service.impl;

import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.dto.member.MemberRequestDto;
import com.wap.cano_be.dto.member.MemberResponseDto;
import com.wap.cano_be.domain.enums.MemberRole;
import com.wap.cano_be.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Member saveMember(MemberRequestDto memberRequestDto) {
        Member member = Member.builder()
                .name(memberRequestDto.name())
                .email(memberRequestDto.email())
                .password(passwordEncoder.encode(memberRequestDto.password()))
                .role(MemberRole.USER).build();
        return memberRepository.save(member);
    }

    public ResponseEntity<ResponseDto> findMyInfo(String name) {
        Optional<Member> member = memberRepository.findByName(name);

        if(member.isEmpty()){
            return MemberResponseDto.noSuchUser();
        }

        MemberResponseDto responseDto = new MemberResponseDto(member.get().getName(), member.get().getEmail(), member.get().getSocialId(), member.get().getProfileImageUrl());
        return ResponseEntity.ok().body(responseDto);
    }
}
