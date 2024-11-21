package com.wap.cano_be.dto.member;

import com.wap.cano_be.common.ResponseCode;
import com.wap.cano_be.common.ResponseDto;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.enums.Degree;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class MemberResponseDto extends ResponseDto {
    private String name;
    private String email;
    private Long socialId;
    private String profileImageUrl;
    private Degree acidity;
    private Degree body;
    private Degree bitterness;
    private Degree sweetness;
    private boolean onboarded;

    @Builder
    public MemberResponseDto(String name, String email, Long socialId, String profileImageUrl) {
        super();
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.profileImageUrl = profileImageUrl;
    }

    public MemberResponseDto(Member member) {
        this.name = member.getName();
        this.email= member.getEmail();
        this.socialId = member.getSocialId();
        this.profileImageUrl = member.getProfileImageUrl();
        this.acidity = member.getAcidity();
        this.body = member.getBody();
        this.bitterness = member.getBitterness();
        this.sweetness = member.getSweetness();
        this.onboarded = member.isOnboarded();
    }

    public static ResponseEntity<ResponseDto> duplicatedMember(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.IS_DUPLICATED.name(), "이미 존재하는 유저입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

}
