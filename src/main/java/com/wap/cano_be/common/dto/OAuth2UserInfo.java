package com.wap.cano_be.common.dto;

import jakarta.security.auth.message.AuthException;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile
) {
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }


}
