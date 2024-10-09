package com.wap.cano_be.member.controller;

import com.wap.cano_be.common.authority.TokenInfo;
import com.wap.cano_be.common.dto.BaseResponse;
import com.wap.cano_be.common.dto.CustomUser;
import com.wap.cano_be.member.dto.LoginDto;
import com.wap.cano_be.member.dto.MemberRequestDto;
import com.wap.cano_be.member.dto.MemberResponseDto;
import com.wap.cano_be.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<String>> signUp(@RequestBody @Valid MemberRequestDto memberRequestDto){
        return ResponseEntity.ok(memberService.signUp(memberRequestDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<TokenInfo>> login(@RequestBody @Valid LoginDto loginDto){
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    // 내 정보 조회
    @GetMapping("/info")
    public ResponseEntity<BaseResponse<MemberResponseDto>> searchMyInfo(@AuthenticationPrincipal CustomUser user){
        long userId = user.getUserId();;
        return ResponseEntity.ok(memberService.searchMyInfo(userId));
    }
}
