package com.wap.cano_be.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponseDto {
    private final String accessToken;
    private final String refreshToken;
}
