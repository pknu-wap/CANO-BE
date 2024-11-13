package com.wap.cano_be.service;

import com.wap.cano_be.dto.auth.LoginRequestDto;
import com.wap.cano_be.dto.auth.LoginResponseDto;
import com.wap.cano_be.dto.auth.ReissueRequestDto;
import com.wap.cano_be.dto.auth.ReissueResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponseDto> kakaoLogin(LoginRequestDto requestDto);
    ResponseEntity<ReissueResponseDto> reissue(ReissueRequestDto requestDto);
}
