package com.wap.cano_be.member.controller;

import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.member.domain.Member;
import com.wap.cano_be.member.domain.MemberRequestDto;
import com.wap.cano_be.member.domain.MemberResponseDto;
import com.wap.cano_be.member.domain.PrincipalDetail;
import com.wap.cano_be.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signUp(@RequestBody MemberRequestDto memberRequestDto) {
        log.info("--------------------------- MemberController ---------------------------");
        log.info("memberDTO = {}", memberRequestDto);

        Optional<Member> byEmail = memberService.findByEmail(memberRequestDto.email());
        if (byEmail.isPresent()) {
            return MemberResponseDto.duplicatedMember();
        } else {
            memberService.saveMember(memberRequestDto);
        }
        return ResponseEntity.ok().body(new ResponseDto());
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseDto> user(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        String email = principalDetail.getName();
        return memberService.findMyInfo(email);
    }

}
