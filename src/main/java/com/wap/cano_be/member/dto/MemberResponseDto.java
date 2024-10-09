package com.wap.cano_be.member.dto;

public record MemberResponseDto(
        Long id,
        String loginId,
        String name,
        String profile,
        String email
) {
}
