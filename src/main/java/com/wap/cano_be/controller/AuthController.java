package com.wap.cano_be.controller;

import com.wap.cano_be.dto.auth.LoginRequestDto;
import com.wap.cano_be.service.AuthService;
import com.wap.cano_be.service.impl.KakaoOAuth2LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/callback-test/kakao")
    public String callbackTest(@RequestParam("code") String code) {
        return "Authorization Code: " + code;
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<?> getUserInfo(@RequestBody LoginRequestDto requestDto) {
        return authService.kakaoLogin(requestDto);
    }
}
