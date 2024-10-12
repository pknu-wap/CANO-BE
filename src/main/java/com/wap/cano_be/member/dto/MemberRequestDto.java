package com.wap.cano_be.member.dto;

public record MemberRequestDto(
        String email,
        String password,
        String name
) {
}