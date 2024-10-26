package com.wap.cano_be.service;

import com.wap.cano_be.dto.oauth2.OAuth2LoginDto;
import com.wap.cano_be.dto.oauth2.TestLoginDto;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface OAuth2LoginService<T> {
    ResponseEntity<T> oauth2Login(OAuth2LoginDto oAuth2LoginDto);
    JSONObject getUserInfo(TestLoginDto requestDto);
}
