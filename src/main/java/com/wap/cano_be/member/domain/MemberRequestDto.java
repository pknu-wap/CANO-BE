package com.wap.cano_be.member.domain;

public record MemberRequestDto(
        String email,
        String password,
        String name
) {
}