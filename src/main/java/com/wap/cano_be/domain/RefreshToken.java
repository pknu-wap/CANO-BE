package com.wap.cano_be.domain;

import jakarta.persistence.*;

@Entity
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(length = 1500)
    private String refreshToken;

    public RefreshToken() {
    }

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
