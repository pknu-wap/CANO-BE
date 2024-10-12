package com.wap.cano_be.member.controller;

import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.member.domain.*;
import com.wap.cano_be.member.dto.MemberRequestDto;
import com.wap.cano_be.member.dto.MemberResponseDto;
import com.wap.cano_be.oauth2.dto.OAuth2LoginDto;
import com.wap.cano_be.member.service.MemberService;
import com.wap.cano_be.oauth2.service.OAuth2LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final OAuth2LoginService oAuth2LoginService;

    @Autowired
    public MemberController(MemberService memberService, OAuth2LoginService oAuth2LoginService){
        this.memberService = memberService;
        this.oAuth2LoginService = oAuth2LoginService;
    }

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

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto> login(@RequestBody OAuth2LoginDto oAuth2LoginDto) {
        switch (oAuth2LoginDto.provider()){
            case "kakao":
                return oAuth2LoginService.oauth2Login(oAuth2LoginDto);
            default:
                return ResponseEntity.badRequest().body(new ResponseDto("UN_SUPPORT", "지원하지 않는 Provider 입니다."));
        }

    }

    @GetMapping("/user")
    public ResponseEntity<ResponseDto> user(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        String email = principalDetail.getName();
        return memberService.findMyInfo(email);
    }

}
