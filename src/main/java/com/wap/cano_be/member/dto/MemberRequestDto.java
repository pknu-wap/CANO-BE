package com.wap.cano_be.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wap.cano_be.common.status.ROLE;
import com.wap.cano_be.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberRequestDto(
        long id,

        @NotBlank
        @JsonProperty("loginId")
        String loginId,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$",
                message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요"
        )
        @JsonProperty("password")
        String password,

        @NotBlank
        @JsonProperty("name")
        String name,

        @JsonProperty("profile")
        String profile,

        @NotBlank
        @Email
        @JsonProperty("email")
        String email
) {
        public Member toEntity(){
                return Member.builder()
                        .id(id)
                        .name(name)
                        .loginId(loginId)
                        .email(email)
                        .password(password)
                        .role(ROLE.MEMBER)
                        .profile(profile)
                        .build();
        }
}
