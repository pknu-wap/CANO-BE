package com.wap.cano_be.oauth2.service;

import com.wap.cano_be.oauth2.dto.OAuth2LoginDto;
import org.springframework.http.ResponseEntity;

public interface OAuth2LoginService<T> {
    ResponseEntity<T> oauth2Login(OAuth2LoginDto oAuth2LoginDto);
}
