package com.wap.cano_be.service;

import com.google.gson.JsonObject;
import com.wap.cano_be.dto.auth.OAuth2LoginDto;
import com.wap.cano_be.dto.auth.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface OAuth2LoginService<T> {
    ResponseEntity<T> oauth2Login(OAuth2LoginDto oAuth2LoginDto);
    JsonObject getUserInfoFromToken(LoginRequestDto requestDto);
}
