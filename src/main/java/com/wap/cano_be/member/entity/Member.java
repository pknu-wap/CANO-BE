package com.wap.cano_be.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_login_id", columnNames = {"loginId"})
        }
)
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20)
    private String loginId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<MemberRole> memberRoles;

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
