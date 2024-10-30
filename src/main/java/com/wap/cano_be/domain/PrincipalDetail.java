package com.wap.cano_be.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails {

    private Member member;
    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    public PrincipalDetail(Member member, Collection<? extends GrantedAuthority> authorities) {
        this.member = member;
        this.authorities = authorities;
    }

    public PrincipalDetail(Member member, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.member = member;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    // info 에 들어가는 것들이 토큰에 들어가는 데이터
    public Map<String, Object> getMemberInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", member.getName());
        if (member.getEmail().isEmpty()) {
            info.put("socialId", member.getSocialId());
        } else {
            info.put("email", member.getEmail());
        }
        info.put("role", member.getRole());
        return info;
    }

    public String getName() {
        return member.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
