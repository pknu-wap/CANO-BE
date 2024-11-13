package com.wap.cano_be.controller;

import com.wap.cano_be.dto.auth.LoginRequestDto;
import com.wap.cano_be.service.impl.KakaoOAuth2LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final KakaoOAuth2LoginService oAuth2LoginService;

    @GetMapping("/oauth2/callback-test")
    public String callbackTest(@RequestParam("code") String code) {
        return "Authorization Code: " + code;
    }

    @PostMapping("/oauth2/login/kakao")
    public ResponseEntity<?> getUserInfo(@RequestBody LoginRequestDto requestDto) {
        return oAuth2LoginService.kakaoLogin(requestDto);
    }
}
