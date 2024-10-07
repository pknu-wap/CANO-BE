package com.wap.cano_be.member.entity;

import com.wap.cano_be.common.status.ROLE;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class MemberRole {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ROLE role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_member_role_member_id"))
    private Member member;

    public MemberRole() {}

    public MemberRole(ROLE role, Member member){
        this.role = role;
        this.member = member;
    }

    public MemberRole(Long id, ROLE role, Member member){
        this.id = id;
        this.role = role;
        this.member = member;
    }
}
