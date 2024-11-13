package com.wap.cano_be.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refresh_token", timeToLive = 604800)
public class RefreshToken {
    @Id
    private String refreshToken;

    private Long userId;

    public RefreshToken() {
    }

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
