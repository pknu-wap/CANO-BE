package com.wap.cano_be.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_login_id", columnNames = {"loginId"})
        }
)
public class Member {
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Column(nullable = false, length = 20)
    private String loginId;

    @Getter
    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 100)
    private String password;

    @Getter
    @Column(nullable = false, length = 30)
    private String email;

    public Member() {}

    @Builder
    public Member(Long id, String loginId, String name, String password, String email){
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
