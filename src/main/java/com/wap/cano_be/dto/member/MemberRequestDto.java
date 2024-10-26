package com.wap.cano_be.dto.member;

public record MemberRequestDto(
        String email,
        String password,
        String name
) {
}