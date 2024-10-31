package com.wap.cano_be.domain;

import jakarta.persistence.*;

@Entity
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String refreshToken;

    @OneToOne
    private Member member;

    public RefreshToken() {
    }

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
