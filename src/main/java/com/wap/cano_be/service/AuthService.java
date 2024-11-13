package com.wap.cano_be.service;

import com.wap.cano_be.dto.auth.LoginRequestDto;
import com.wap.cano_be.dto.auth.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponseDto> kakaoLogin (LoginRequestDto requestDto);
}
