package com.wap.cano_be.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_like")
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public Like() {}

    public Like(Member member, Menu menu) {
        this.member = member;
        this.menu = menu;
    }
}
