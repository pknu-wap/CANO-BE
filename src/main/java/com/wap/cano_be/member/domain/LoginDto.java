package com.wap.cano_be.member.domain;

public record LoginDto(
        String token,
        String providerId
) {
}
