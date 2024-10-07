package com.wap.cano_be.common.authority;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    // 토큰 만료 시간 (밀리초 단위)
    private static final long EXPIRATION_MILLISECOND = 1000 * 60 * 30;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // Token 생성
    public TokenInfo createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date accessExpiration = new Date(now.getTime() + EXPIRATION_MILLISECOND);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("userId", ((CustomUser) authentication.getPrincipal().getUserId()))
                .setIssuedAt(now)
                .setExpiration(accessExpiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return new TokenInfo("Bearer", accessToken);
    }

    // Token 정보 추출
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String auth = (String) claims.get("auth");
        if(auth == null){
            throw new RuntimeException("잘못된 토큰입니다.");
        }

        Long userId = claims.get("userId", Long.class);
        if(userId == null){
            throw new RuntimeException("잘못된 토큰입니다.");
        }

        // 권한 정보 추출
        Collection<GrantedAuthority> authorities =
                auth.lines().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UserDetails principal = new CustomUser(userId, claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Token 검증
    public boolean validateToken(String token){
        try {
            getClaims(token);
            return true;
        } catch (Exception e){
            if(e instanceof SecurityException){}
            else if(e instanceof MalformedJwtException){}
            else if(e instanceof ExpiredJwtException){}
            else if(e instanceof UnsupportedJwtException){}
            else if(e instanceof IllegalArgumentException){}
            System.out.println(e.getMessage());
        }
        return false;
    }


}
