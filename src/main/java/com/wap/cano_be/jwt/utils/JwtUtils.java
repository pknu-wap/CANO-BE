package com.wap.cano_be.jwt.utils;

import ghkwhd.security.jwt.exception.CustomExpiredJwtException;
import ghkwhd.security.jwt.exception.CustomJwtException;
import ghkwhd.security.member.domain.Member;
import ghkwhd.security.member.domain.MemberRole;
import ghkwhd.security.member.domain.PrincipalDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class JwtUtils {

    @Value("${jwt.secret}")
    public static String secretKey;

    private static Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 헤더에 "Bearer XXX" 형식으로 담겨온 토큰을 추출한다
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public static String generateToken(Map<String, Object> valueMap, int validTime) {
//        try {
//
//        } catch(Exception e){
//            throw new RuntimeException(e.getMessage());
//        }
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
//        Key key = Keys.hmacShaKeyFor(JwtUtils.secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
