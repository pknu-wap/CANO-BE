package com.wap.cano_be.oauth2.dto;

public record OAuth2LoginDto(
        String token,
        String provider
) {
}
