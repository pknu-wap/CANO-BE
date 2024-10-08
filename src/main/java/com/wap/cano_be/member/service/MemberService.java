package com.wap.cano_be.member.service;

import com.wap.cano_be.common.authority.JwtTokenProvider;
import com.wap.cano_be.common.authority.TokenInfo;
import com.wap.cano_be.member.entity.Member;
import com.wap.cano_be.member.repository.MemberRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 회원가입
    public String signUp(MemberDtoRequest memberDtoRequest){
        // ID 중복 검사
        Optional<Member> optionalMember = memberRepository.findByLoginId(memberDtoRequest.getLoginId());
        if(optionalMember.isPresent()){
            throw new InvalidInputException("loginId", "이미 등록된 ID 입니다.");
        }

        Member member = memberDtoRequest.toEntity();
        memberRepository.save(member);

        return "회원가입이 완료되었습니다.";
    }

    // 로그인, 토큰 발행
    public TokenInfo login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.createAccessToken(authentication);
    }

}
