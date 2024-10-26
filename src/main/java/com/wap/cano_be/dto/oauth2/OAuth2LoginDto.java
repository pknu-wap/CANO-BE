package com.wap.cano_be.dto.oauth2;

public record OAuth2LoginDto(
        String token,
        String provider
) {
}
