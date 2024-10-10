package com.wap.cano_be.oauth2.service;

import com.wap.cano_be.member.domain.Member;
import com.wap.cano_be.member.domain.MemberRole;
import com.wap.cano_be.member.domain.PrincipalDetail;
import com.wap.cano_be.member.repository.MemberRepository;
import com.wap.cano_be.oauth2.user.OAuth2UserInfo;
import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("--------------------------- OAuth2UserService ---------------------------");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("OAuth2User = {}", oAuth2User);
        log.info("attributes = {}", attributes);

        // registrationId
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("ProviderId = {}", registrationId);

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("nameAttributeKey = {}", userNameAttributeName);

        final OAuth2UserInfo oAuth2UserInfo;
        try {
            oAuth2UserInfo = OAuth2UserInfo.of(registrationId, attributes);
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }

        // 소셜 ID 로 사용자를 조회, 없으면 socialId 와 이름으로 사용자 생성
        Optional<Member> bySocialId = memberRepository.findBySocialId(oAuth2UserInfo.socialId());
        Member member = bySocialId.orElseGet(() -> saveSocialMember(oAuth2UserInfo));

        return new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())),
                attributes);
    }

    // 소셜 ID 로 가입된 사용자가 없으면 새로운 사용자를 만들어 저장한다
    public Member saveSocialMember(OAuth2UserInfo oAuth2UserInfo) {
        log.info("--------------------------- saveSocialMember ---------------------------");
        Member newMember = Member.builder()
                .socialId(oAuth2UserInfo.socialId())
                .name(oAuth2UserInfo.name())
                .email(oAuth2UserInfo.email())
                .profileImageUrl(oAuth2UserInfo.profileImageUrl())
                .role(MemberRole.USER)
                .build();
        return memberRepository.save(newMember);
    }
}
