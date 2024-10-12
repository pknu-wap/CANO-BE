package com.wap.cano_be.member.domain;

import jakarta.persistence.*;

@Entity
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String refreshToken;
    private int expireDate;

    @OneToOne
    private Member member;
}
