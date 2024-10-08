package com.wap.cano_be.member.entity;

import com.wap.cano_be.common.status.Gender;
import com.wap.cano_be.common.status.ROLE;
import com.wap.cano_be.member.dto.MemberDtoResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_login_id", columnNames = {"loginId"})
        }
)
@Getter
public class Member implements UserDetails {
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ROLE role;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public MemberDtoResponse toDto(){
        return new MemberDtoResponse(id, loginId, name, profile, email);
    }

    public Member() {}

    @Builder
    public Member(Long id, String loginId, String name, String profile, String password, String email, Gender gender, ROLE role){
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.profile = profile;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.role = role;
    }
}
