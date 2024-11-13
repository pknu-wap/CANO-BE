package com.wap.cano_be.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.enums.MemberRole;
import com.wap.cano_be.dto.auth.LoginRequestDto;
import com.wap.cano_be.dto.auth.LoginResponseDto;
import com.wap.cano_be.repository.MemberRepository;
import com.wap.cano_be.security.JwtConstants;
import com.wap.cano_be.security.JwtUtils;
import com.wap.cano_be.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<LoginResponseDto> kakaoLogin(LoginRequestDto requestDto) {
        JsonObject response = getUserInfoFromToken(requestDto.getToken());

        // 카카오 토큰에서 이메일 추출
        String email = response.get("kakao_account").getAsJsonObject().get("email").getAsString();

        // 이메일로 Member 검색
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        Member member;
        if (memberOptional.isPresent()) {
            // 기존 멤버가 있는 경우, 멤버 정보 사용
            member = memberOptional.get();
        } else {
            // Member가 없는 경우 새 멤버 추가
            member = Member.builder()
                    .email(email)
                    .socialId(response.get("id").getAsString())
                    .providerId("kakao")
                    .role(MemberRole.USER)
                    .build();

            memberRepository.save(member);
        }

        // AccessToken 생성
        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put("email", member.getEmail());
        accessTokenClaims.put("role", member.getRole());
        String accessToken = JwtUtils.generateToken(accessTokenClaims, JwtConstants.ACCESS_EXP_TIME);

        // RefreshToken 생성
        // AccessToken 생성
        Map<String, Object> refreshTokenClaims = new HashMap<>();
        refreshTokenClaims.put("email", member.getEmail());
        refreshTokenClaims.put("role", member.getRole());
        String refreshToken = JwtUtils.generateToken(refreshTokenClaims, JwtConstants.REFRESH_EXP_TIME)

        return ResponseEntity.ok().body(new LoginResponseDto(accessToken, refreshToken));
    }

    private JsonObject getUserInfoFromToken(String token) {
        final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        Gson gson = new Gson();

        return gson.fromJson(response.getBody(), JsonObject.class);
    }
}
