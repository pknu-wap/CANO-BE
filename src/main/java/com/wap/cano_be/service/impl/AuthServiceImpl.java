package com.wap.cano_be.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.RefreshToken;
import com.wap.cano_be.domain.enums.MemberRole;
import com.wap.cano_be.dto.auth.LoginRequestDto;
import com.wap.cano_be.dto.auth.LoginResponseDto;
import com.wap.cano_be.dto.auth.ReissueRequestDto;
import com.wap.cano_be.dto.auth.ReissueResponseDto;
import com.wap.cano_be.repository.MemberRepository;
import com.wap.cano_be.repository.RefreshTokenRepository;
import com.wap.cano_be.security.JwtConstants;
import com.wap.cano_be.security.JwtUtils;
import com.wap.cano_be.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDto> kakaoLogin(LoginRequestDto requestDto) {
        JsonObject token = getUserInfoFromToken(requestDto.getToken());

        // 카카오 토큰에서 이메일 추출
        String email = extractEmailFromKakaoToken(token);

        // 이메일로 Member 검색 또는 생성
        Member member = getOrCreateMemberFromKakaoToken(email, token);

        // 토큰 생성
        String accessToken = createAccessToken(member, member.getRole());
        String refreshToken = createRefreshTokenAndSave(member, member.getRole());

        return ResponseEntity.ok().body(new LoginResponseDto(accessToken, refreshToken));
    }

    private String createAccessToken(Member member, MemberRole role) {
        Map<String, Object> accessTokenClaims = new HashMap<>();
        accessTokenClaims.put("id", member.getId().toString());
        accessTokenClaims.put("email", member.getEmail());
        accessTokenClaims.put("role", role);
        return JwtUtils.generateToken(accessTokenClaims, JwtConstants.ACCESS_EXP_TIME);
    }

    private String createRefreshTokenAndSave(Member member, MemberRole role) {
        Map<String, Object> refreshTokenClaims = new HashMap<>();
        refreshTokenClaims.put("id", member.getId().toString());
        refreshTokenClaims.put("email", member.getEmail());
        refreshTokenClaims.put("role", role);
        String refreshToken = JwtUtils.generateToken(refreshTokenClaims, JwtConstants.REFRESH_EXP_TIME);

        RefreshToken refreshTokenForRedis = new RefreshToken(refreshToken, member.getId());
        refreshTokenRepository.save(refreshTokenForRedis);

        return refreshToken;
    }

    private String extractEmailFromKakaoToken(JsonObject token) {
        return token.get("kakao_account").getAsJsonObject().get("email").getAsString();
    }

    private Member getOrCreateMemberFromKakaoToken(String email, JsonObject token) {
        return memberRepository.findByEmail(email).orElseGet(() -> createMemberFromKakaoToken(email, token));
    }

    private Member createMemberFromKakaoToken(String email, JsonObject token) {
        Member member = Member.builder()
                .email(email)
                .socialId(token.get("id").getAsLong())
                .providerId("kakao")
                .role(MemberRole.USER)
                .build();
        return memberRepository.save(member);
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

    @Override
    @Transactional
    public ResponseEntity<ReissueResponseDto> reissue(ReissueRequestDto requestDto) {
        Claims claims = JwtUtils.validateToken(requestDto.getRefreshToken());
        Long memberId = Long.valueOf(claims.get("id").toString());

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member with id: " + memberId + " is not found."));

        deleteExistingRefreshToken(requestDto.getRefreshToken());

        String accessToken = createAccessToken(member, member.getRole());
        String refreshToken = createRefreshTokenAndSave(member, member.getRole());

        return ResponseEntity.ok().body(new ReissueResponseDto(accessToken, refreshToken));
    }

    private void deleteExistingRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}
