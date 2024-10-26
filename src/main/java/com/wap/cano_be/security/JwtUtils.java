package com.wap.cano_be.security;

import com.wap.cano_be.exception.CustomExpiredJwtException;
import com.wap.cano_be.exception.CustomJwtException;
import com.wap.cano_be.domain.Member;
import com.wap.cano_be.domain.enums.MemberRole;
import com.wap.cano_be.domain.PrincipalDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secretKeyFromProperty;

    private static String secretKey;
    private static Key key;

    @PostConstruct
    public void init() {
        secretKey = secretKeyFromProperty;

        if (secretKey.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("Secret key must be at least 32 bytes");
        }

        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 헤더에 "Bearer XXX" 형식으로 담겨온 토큰을 추출한다
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public static String generateToken(Map<String, Object> valueMap, int validTime) {
        return Jwts.builder()
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(validTime).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Authentication getAuthentication(String token) {
        Claims claims = validateToken(token);

        String email = (String) claims.get("email");
        String name = (String) claims.get("name");
        String role = (String) claims.get("role");
        MemberRole memberRole = MemberRole.valueOf(role);

        Member member = Member.builder().email(email).name(name).role(memberRole).build();
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue()));
        PrincipalDetail principalDetail = new PrincipalDetail(member, authorities);

        return new UsernamePasswordAuthenticationToken(principalDetail, "", authorities);
    }

    public static Claims validateToken(String token) {
        Claims claim = null;
        try {
            claim = getClaims(token);
        } catch(ExpiredJwtException expiredJwtException){
            throw new CustomExpiredJwtException("토큰이 만료되었습니다", expiredJwtException);
        } catch(Exception e){
            throw new CustomJwtException(e.getMessage());
        }
        return claim;
    }

    // 토큰이 만료되었는지 판단하는 메서드
    public static boolean isExpired(String token) {
        try {
            validateToken(token);
        } catch (Exception e) {
            return (e instanceof CustomExpiredJwtException);
        }
        return false;
    }

    // 토큰의 남은 만료시간 계산
    public static long tokenRemainTime(Integer expTime) {
        Date expDate = new Date((long) expTime * (1000));
        long remainMs = expDate.getTime() - System.currentTimeMillis();
        return remainMs / (1000 * 60);
    }

    private static Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
