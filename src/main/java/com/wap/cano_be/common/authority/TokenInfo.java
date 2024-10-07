package com.wap.cano_be.common.authority;

public record TokenInfo(
        String grantType,
        String accessToken
) {
}
