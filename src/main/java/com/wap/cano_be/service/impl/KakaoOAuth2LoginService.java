package com.wap.cano_be.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wap.cano_be.common.ResponseCode;
import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.security.JwtConstants;
import com.wap.cano_be.security.JwtUtils;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.enums.MemberRole;
import com.wap.cano_be.dto.oauth2.OAuth2LoginDto;
import com.wap.cano_be.dto.oauth2.OAuth2UserResponseDto;
import com.wap.cano_be.repository.MemberRepository;
import com.wap.cano_be.dto.oauth2.TestLoginDto;
import com.wap.cano_be.service.OAuth2LoginService;
import com.wap.cano_be.dto.oauth2.OAuth2UserInfo;
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

    private Map<String, Object> attributes = new HashMap<>();

    @Autowired
    public KakaoOAuth2LoginService(WebClient.Builder weCliendBuilder, MemberRepository memberRepository) {
        this.webClient = weCliendBuilder.baseUrl("https://kapi.kakao.com").build();
        this.memberRepository = memberRepository;
    }

    private void fetchAttributes(OAuth2LoginDto oAuth2LoginDto) {
        if(oAuth2LoginDto.token().isEmpty()){
            log.warn("토큰이 없음");
            attributes.put("NT", "NO_TOKEN");
            return;
        }

        String accessToken = JwtConstants.JWT_TYPE + oAuth2LoginDto.token();
        webClient.get()
                .uri("/v2/user/me")
                .header(JwtConstants.JWT_HEADER, accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(Map.class)
                .subscribe(attributes -> {
                    if (attributes == null) {
                        log.warn("attributes is null, 유저 정보 불러오기 실패");
                        this.attributes.put("CF", "CERTIFICATION_FAIL");
                    } else {
                        this.attributes = attributes;
                        attributes.forEach((k, v) -> {
                            log.info("{} : {}", k, v);
                        });
                    }
                }, error -> {
                    log.error("Error during fetching Kakao user information", error);
                });
    }

    @Override
    public ResponseEntity<ResponseDto> oauth2Login(OAuth2LoginDto oAuth2LoginDto){
        log.info("-------------------KakaoOAuth2APIService----------------------------");
        fetchAttributes(oAuth2LoginDto); // attribute

        if("NO_TOKEN".equals(attributes.get("NT"))){
            return ResponseEntity.badRequest().body(new ResponseDto("NO_TOKEN", "토큰이 없습니다."));
        }

        if("CERTIFICATION_FAIL".equals(attributes.get("CF"))) {
            return OAuth2UserResponseDto.certificationFail();
        }

        OAuth2UserInfo kakaoUserInfo = null;
        try {
            kakaoUserInfo = OAuth2UserInfo.of("kakao", attributes); // OAuth2User 구현체
        } catch (AuthException e) {
            return ResponseEntity.internalServerError().body(new ResponseDto("error", e.getMessage()));
        }

        if(kakaoUserInfo == null){
            log.info("유저 생성에 실패했습니다.");
            return ResponseDto.noSuchUser();
        }

        Optional<Member> findUser = memberRepository.findBySocialId(kakaoUserInfo.socialId());
        OAuth2UserInfo finalKakaoUserInfo = kakaoUserInfo;

        log.info("Token generate");
        String accessToken = JwtUtils.generateToken(attributes, JwtConstants.ACCESS_EXP_TIME);
        log.info("access token = {}", accessToken);
        if(accessToken.isEmpty()){
            return ResponseEntity.internalServerError().body(new ResponseDto(ResponseCode.VALIDATION_FAIL.name(), "Jwt 토큰이 정상적으로 생성되지 않았습니다."));
        }
        String refreshToken = JwtUtils.generateToken(attributes, JwtConstants.REFRESH_EXP_TIME);

        return ResponseEntity.ok().body(OAuth2UserResponseDto.builder()
                        .name(kakaoUserInfo.name())
                        .email(kakaoUserInfo.email())
                        .socialId(kakaoUserInfo.socialId())
                        .profileImageUrl(kakaoUserInfo.profileImageUrl())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build());

//        Member member = findUser.orElseGet(() -> saveSocialMember(finalKakaoUserInfo));
//
//        PrincipalDetail kakaoUserPrincipal =
//                new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())), attributes);
//
//        return ResponseEntity.ok()
//                .header(JwtConstants.JWT_HEADER, JwtUtils.generateToken(attributes, JwtConstants.ACCESS_EXP_TIME))
//                .body(new OAuthUserInfoResponseDto((Map<String, Object>) kakaoUserPrincipal));
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
    public JsonObject getUserInfo(TestLoginDto requestDto) {
        log.info("token: " + requestDto.getToken());
        final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + requestDto.getToken());
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
}
