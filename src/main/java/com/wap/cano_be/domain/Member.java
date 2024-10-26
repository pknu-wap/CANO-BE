package com.wap.cano_be.domain;

import com.wap.cano_be.domain.enums.Gender;
import com.wap.cano_be.domain.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String socialId;
    private String profileImageUrl;
    private String providerId;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    private RefreshToken refreshToken;
}
