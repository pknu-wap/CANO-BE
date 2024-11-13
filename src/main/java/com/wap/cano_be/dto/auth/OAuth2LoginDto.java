package com.wap.cano_be.dto.auth;

public record OAuth2LoginDto(
        String token,
        String provider
) {
}
