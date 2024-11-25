package com.wap.cano_be.domain;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "menu_like")
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public Like() {}

    @Builder
    public Like(Member member, Menu menu) {
        this.member = member;
        this.menu = menu;
    }
}
