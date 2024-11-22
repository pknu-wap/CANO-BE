package com.wap.cano_be.service.impl;

import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.dto.member.MemberRequestDto;
import com.wap.cano_be.dto.member.MemberResponseDto;
import com.wap.cano_be.domain.enums.MemberRole;
import com.wap.cano_be.dto.member.MemberUpdateRequestDto;
import com.wap.cano_be.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member saveMember(MemberRequestDto memberRequestDto) {
        Member member = Member.builder()
                .name(memberRequestDto.name())
                .email(memberRequestDto.email())
                .password(passwordEncoder.encode(memberRequestDto.password()))
                .role(MemberRole.USER).build();
        return memberRepository.save(member);
    }

    public ResponseEntity<ResponseDto> getMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);

        if(member.isEmpty()){
            return MemberResponseDto.noSuchUser();
        }

        MemberResponseDto responseDto = new MemberResponseDto(member.get().getName(), member.get().getEmail(), member.get().getSocialId(), member.get().getProfileImageUrl());
        return ResponseEntity.ok().body(responseDto);
    }

    public ResponseEntity<MemberResponseDto> updateMember(Long memberId, MemberUpdateRequestDto requestDto, MultipartFile image) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found. memberId: " + memberId));
        member.setName(requestDto.getName());

        if (requestDto.getAcidity() != null) {
            member.setAcidity(requestDto.getAcidity());
        }
        if (requestDto.getBody() != null) {
            member.setBody(requestDto.getBody());
        }
        if (requestDto.getBitterness() != null) {
            member.setBitterness(requestDto.getBitterness());
        }
        if (requestDto.getSweetness() != null) {
            member.setSweetness(requestDto.getSweetness());
        }

        if (image != null && !image.isEmpty()) {
            // 기존 이미지 삭제
            if (member.getProfileImageUrl() != null) {
                imageService.deleteImage(member.getProfileImageUrl());
            }

            // 새 이미지 업로드
            String imageUrl = imageService.uploadImage(image);
            member.setProfileImageUrl(imageUrl);
        }

        member.setOnboarded(true);

        member = memberRepository.save(member);
        return ResponseEntity.ok().body(new MemberResponseDto(member));
    }
}
