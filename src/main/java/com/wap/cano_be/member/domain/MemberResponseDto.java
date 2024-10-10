package com.wap.cano_be.member.domain;

import com.wap.cano_be.common.ResponseCode;
import com.wap.cano_be.common.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class MemberResponseDto extends ResponseDto {
    private String name;
    private String email;
    private String socialId;
    private String profileImageUrl;

    @Builder
    public MemberResponseDto(String name, String email, String socialId, String profileImageUrl) {
        super();
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.profileImageUrl = profileImageUrl;
    }

    public static ResponseEntity<ResponseDto> duplicatedMember(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.IS_DUPLICATED.name(), "이미 존재하는 유저입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

}
