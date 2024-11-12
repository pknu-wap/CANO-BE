package com.wap.cano_be.domain;

import jakarta.persistence.*;

@Entity
/**
 * Menu - Member 의 Join 테이블
 */
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
