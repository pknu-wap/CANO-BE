package com.wap.cano_be.member.service;

import com.wap.cano_be.common.authority.JwtTokenProvider;
import com.wap.cano_be.common.authority.TokenInfo;
import com.wap.cano_be.common.dto.BaseResponse;
import com.wap.cano_be.common.exception.InvalidInputException;
import com.wap.cano_be.common.status.ResultCode;
import com.wap.cano_be.member.dto.LoginDto;
import com.wap.cano_be.member.dto.MemberRequestDto;
import com.wap.cano_be.member.dto.MemberResponseDto;
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
    public BaseResponse<String> signUp(MemberRequestDto memberRequestDto){
        // ID 중복 검사
        Optional<Member> optionalMember = memberRepository.findByLoginId(memberRequestDto.loginId());
        if(optionalMember.isPresent()){
            throw new InvalidInputException("loginId", "이미 등록된 ID 입니다.");
        }

        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);

        return new BaseResponse<>(ResultCode.SUCCESS.name(), "회원가입이 완료되었습니다.", ResultCode.SUCCESS.message());
    }

    // 로그인, 토큰 발행
    public BaseResponse<TokenInfo> login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.loginId(), loginDto.password());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return new BaseResponse<>(ResultCode.SUCCESS.name(), jwtTokenProvider.createAccessToken(authentication), ResultCode.SUCCESS.message());
    }

    // 내 정보 조회
    public BaseResponse<MemberResponseDto> searchMyInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("id", "회원번호: " + id + "는 존재하지 않는 유저입니다."));

        MemberResponseDto responseDto = new MemberResponseDto(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getEmail(),
                member.getProfileImageUrl()
        );

        return new BaseResponse<>(ResultCode.SUCCESS.name(), responseDto, ResultCode.SUCCESS.message());
    }

    // 내 정보 수정
    public BaseResponse<String> editMyInfo(MemberRequestDto memberRequestDto){
        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);
        return new BaseResponse<>(ResultCode.SUCCESS.name(), "수정이 완료되었습니다.", ResultCode.SUCCESS.message());
    }

}
