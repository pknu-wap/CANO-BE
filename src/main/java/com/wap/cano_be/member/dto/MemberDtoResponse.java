package com.wap.cano_be.member.dto;

public record MemberDtoResponse(
        Long id,
        String loginId,
        String name,
        String profile,
        String email
) {
}
