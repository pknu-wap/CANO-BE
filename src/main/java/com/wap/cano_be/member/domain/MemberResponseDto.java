package com.wap.cano_be.member.domain;

import com.wap.cano_be.common.ResponseDto;
import lombok.Builder;
import lombok.Getter;

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

}
