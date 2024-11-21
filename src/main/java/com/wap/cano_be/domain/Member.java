package com.wap.cano_be.domain;

import com.wap.cano_be.domain.enums.Degree;
import com.wap.cano_be.domain.enums.Gender;
import com.wap.cano_be.domain.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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
    private Long socialId;
    private String profileImageUrl;
    private Degree acidity;
    private Degree body;
    private Degree bitterness;
    private Degree sweetness;
    @Setter
    private String providerId;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
