package com.wap.cano_be.member.controller;

import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.member.domain.Member;
import com.wap.cano_be.member.domain.MemberDTO;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public Map<String, String> signUp(@RequestBody MemberDTO memberDTO) {
        log.info("--------------------------- MemberController ---------------------------");
        log.info("memberDTO = {}", memberDTO);
        Map<String, String> response = new HashMap<>();
        Optional<Member> byEmail = memberService.findByEmail(memberDTO.getEmail());
        if (byEmail.isPresent()) {
            response.put("error", "이미 존재하는 이메일입니다");
        } else {
            memberService.saveMember(memberDTO);
            response.put("success", "성공적으로 처리하였습니다");
        }
        return response;
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseDto> user(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        String email = principalDetail.getName();
        return memberService.findMyInfo(email);
    }

}
