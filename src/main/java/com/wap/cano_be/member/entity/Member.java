package com.wap.cano_be.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
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

    private String profile;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<MemberRole> memberRoles = new ArrayList<>();

    public Member() {}

    @Builder
    public Member(Long id, String loginId, String name, String profile, String password, String email, MemberRole role){
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.profile = profile;
        this.password = password;
        this.email = email;
        memberRoles.add(role);
    }
}
