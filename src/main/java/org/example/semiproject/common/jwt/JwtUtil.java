package org.example.semiproject.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // JWT 비밀키 (서명 및 검증에 사용, 32자 이상 필요)
    private final String SECRET_KEY = "*itcen-student-20250428-202506017*"; // 32자 이상

    // 토큰 만료 시간 (1시간, 밀리초 단위)
    private final long EXPIRATION = 1000 * 60 * 60; // 1시간

    /**
     * JWT 서명을 위한 Key 객체 반환
     * - SECRET_KEY를 바이트 배열로 변환하여 Key로 생성
     * - HMAC SHA 알고리즘 사용
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * JWT 토큰 생성
     * @param username 사용자 아이디 등 subject에 들어갈 값
     * @return 생성된 JWT 토큰 문자열
     *
     * - subject(토큰주체), 발급시간, 만료시간, 서명 정보 등 포함
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 사용자명(주체)을 subject에 저장
                .setIssuedAt(new Date()) // 현재 시간 기준 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 만료 시간 설정(현재+1시간)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 키와 알고리즘으로 서명
                .compact(); // JWT 문자열 생성 및 반환
    }

    /**
     * JWT 토큰에서 사용자명(subject) 추출
     * @param token JWT 토큰 문자열
     * @return 토큰에 저장된 사용자명(subject)
     *
     * - 서명 검증 후 Claims에서 추출
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 서명 검증을 위한 키 등록
                .build()
                .parseClaimsJws(token) // 토큰 검증 및 파싱
                .getBody() // Claims 정보 가져오기
                .getSubject(); // subject(사용자명) 추출
    }

    /**
     * JWT 토큰 유효성 검증
     * @param token JWT 토큰 문자열
     * @return 토큰이 유효하면 true, 아니면 false
     *
     * - 서명 및 만료 시간 등 검증
     * - 예외 발생 시 false 반환
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // 서명 검증용 키 등록
                    .build()
                    .parseClaimsJws(token); // 토큰 검증 (서명/만료 등)
            return true; // 검증 성공
        } catch (JwtException | IllegalArgumentException e) {
            // 서명 오류, 만료 등 예외 발생하면 false
            return false;
        }
    }

}
