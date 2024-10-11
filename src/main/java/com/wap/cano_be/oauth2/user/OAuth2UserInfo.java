package com.wap.cano_be.oauth2.user;

import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Builder
public record OAuth2UserInfo(
        String socialId,
        String name,
        String email,
        String profileImageUrl
) {
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) throws AuthException {
        return switch (registrationId){
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(registrationId + " 해당 인증은 지원하지 않습니다.");
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes){
        return OAuth2UserInfo.builder()
                .socialId((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImageUrl((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes){
        log.info("---------OAuth2UserInfo-------------");
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        log.info("kakao account = {}", account);
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        log.info("kakao profile = {}", profile);
        return OAuth2UserInfo.builder()
                .socialId(String.valueOf(attributes.get("id")))
                .name((String) profile.get("nickname"))
//                .email((String) account.get("email"))
                .profileImageUrl((String) profile.get("picture"))
                .build();
    }
}
