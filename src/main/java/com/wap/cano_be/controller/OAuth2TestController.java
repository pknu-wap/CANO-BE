package com.wap.cano_be.controller;

import com.wap.cano_be.dto.oauth2.TestLoginDto;
import com.wap.cano_be.service.impl.KakaoOAuth2LoginService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuth2TestController {
    private final KakaoOAuth2LoginService oAuth2LoginService;

    @GetMapping("/oauth2/callback-test")
    public String callbackTest(@RequestParam("code") String code) {
        return "Authorization Code: " + code;
    }

    @PostMapping("/oauth2/login/kakao")
    public String getUserInfo(@RequestBody TestLoginDto requestDto) {
        JSONObject userInfo = oAuth2LoginService.getUserInfo(requestDto);
        return userInfo.toString();
    }
}
