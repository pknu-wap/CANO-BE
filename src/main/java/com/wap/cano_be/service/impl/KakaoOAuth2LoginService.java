package com.wap.cano_be.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wap.cano_be.common.ResponseCode;
import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.domain.RefreshToken;
import com.wap.cano_be.repository.RefreshTokenRepository;
import com.wap.cano_be.security.JwtConstants;
import com.wap.cano_be.security.JwtUtils;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.enums.MemberRole;
import com.wap.cano_be.dto.auth.OAuth2LoginDto;
import com.wap.cano_be.dto.auth.OAuth2UserResponseDto;
import com.wap.cano_be.repository.MemberRepository;
import com.wap.cano_be.dto.auth.LoginRequestDto;
import com.wap.cano_be.service.OAuth2LoginService;
import com.wap.cano_be.dto.auth.OAuth2UserInfo;
import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class KakaoOAuth2LoginService implements OAuth2LoginService {
    private final WebClient webClient;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private Map<String, Object> attributes = new HashMap<>();

    @Autowired
    public KakaoOAuth2LoginService(WebClient.Builder weCliendBuilder, MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository) {
        this.webClient = weCliendBuilder.baseUrl("https://kapi.kakao.com").build();
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private void fetchAttributes(OAuth2LoginDto oAuth2LoginDto) {
        log.info("=====fecthAttributes=====");
        if(oAuth2LoginDto.token().isEmpty()){
            log.warn("토큰이 없음");
            attributes.put("NT", "NO_TOKEN");
            return;
        }

        String accessToken = JwtConstants.JWT_TYPE + oAuth2LoginDto.token();
        Map<String, Object> attributes = webClient.get()
                .uri("/v2/user/me")
                .header(JwtConstants.JWT_HEADER, accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if(attributes.isEmpty()){
            log.warn("attributes is null, 유저 정보 불러오기 실패");
            this.attributes.put(ResponseCode.CERTIFICATION_FAIL.name(), ResponseCode.CERTIFICATION_FAIL.message());
        } else {
            this.attributes = attributes;
            attributes.forEach((k,v) -> {
                log.info("{} : {}", k, v);
            });
        }
    }

    @Override
    public ResponseEntity<ResponseDto> oauth2Login(OAuth2LoginDto oAuth2LoginDto){
//        log.info("-------------------KakaoOAuth2APIService----------------------------");
//        fetchAttributes(oAuth2LoginDto); // attribute
//
//        if("NO_TOKEN".equals(attributes.get("NT"))){
//            return ResponseEntity.badRequest().body(new ResponseDto("NO_TOKEN", "토큰이 없습니다."));
//        }
//
//        if("CERTIFICATION_FAIL".equals(attributes.get("CF"))) {
//            return OAuth2UserResponseDto.certificationFail();
//        }
//
//        if(ResponseCode.VALIDATION_FAIL.name().equals(attributes.get(ResponseCode.VALIDATION_FAIL.name()))){
//            return ResponseDto.validationFail();
//        }
//
//        OAuth2UserInfo kakaoUserInfo = null;
//        try {
//            log.info("=====OAuth2UserInfo 생성=====");
//            kakaoUserInfo = OAuth2UserInfo.of("kakao", attributes); // OAuth2User 구현체
//        } catch (AuthException e) {
//            log.warn("=====!OAuth2UserInfo 생성 실패!=====");
//            return ResponseEntity.internalServerError().body(new ResponseDto("error", e.getMessage()));
//        }
//
//        if(kakaoUserInfo == null){
//            log.info("유저 생성에 실패했습니다.");
//            return ResponseDto.noSuchUser();
//        }
//
//        Optional<Member> findUser = memberRepository.findBySocialId(kakaoUserInfo.socialId());
//        OAuth2UserInfo finalKakaoUserInfo = kakaoUserInfo;
//
//        log.info("Token generate");
//
//        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
//        Map<String, Object> claim = new HashMap<>();
//
//        claim.put("id", attributes.get("id"));
//        claim.put("name", profile.get("nickname"));
//        claim.put("email", account.get("email"));
//        claim.put("role", "USER");
//
//        String accessToken = JwtUtils.generateToken(claim, JwtConstants.ACCESS_EXP_TIME);
//        log.info("access token = {}", accessToken);
//        if(accessToken.isEmpty()){
//            return ResponseEntity.internalServerError().body(new ResponseDto(ResponseCode.VALIDATION_FAIL.name(), "Jwt 토큰이 정상적으로 생성되지 않았습니다."));
//        }
//
//        RefreshToken refreshToken = new RefreshToken(JwtUtils.generateToken(claim, JwtConstants.REFRESH_EXP_TIME));
//        refreshTokenRepository.save(refreshToken);
//
//        Member member = findUser.orElseGet(() -> saveSocialMember(finalKakaoUserInfo));
//        member.setRefreshToken(refreshToken);
//        member.setProviderId("kakao");
//
//        return ResponseEntity.ok().header(JwtConstants.JWT_HEADER, accessToken).body(new ResponseDto());
        return null;
    }

    // 소셜 ID 로 가입된 사용자가 없으면 새로운 사용자를 만들어 저장한다
    private Member saveSocialMember(OAuth2UserInfo oAuth2UserInfo) {
        log.info("카카오 유저로 회원가입");
        Member newMember = Member.builder()
                .socialId(oAuth2UserInfo.socialId())
                .name(oAuth2UserInfo.name())
                .email(oAuth2UserInfo.email())
                .profileImageUrl(oAuth2UserInfo.profileImageUrl())
                .providerId("kakao")
                .role(MemberRole.USER)
                .build();
        return memberRepository.save(newMember);
    }

    @Override
    public JsonObject getUserInfoFromToken(LoginRequestDto loginRequestDto) {
        log.info("token: " + loginRequestDto.getToken());
        final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + loginRequestDto.getToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        log.info("response: " + response.getBody());

        Gson gson = new Gson();

        return gson.fromJson(response.getBody(), JsonObject.class);
    }

    public ResponseEntity<?> kakaoLogin(LoginRequestDto loginRequestDto) {
        JsonObject response = getUserInfoFromToken(loginRequestDto);
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

        // AccessToken과 함께 ResponseEntity 반환
        Map<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);

        return ResponseEntity.ok(token);
    }
}
