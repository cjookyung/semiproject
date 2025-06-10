package org.example.semiproject.config;

import lombok.RequiredArgsConstructor;
import org.example.semiproject.common.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross-Site Request Forgery) 보호 기능 비활성화 (JWT 기반 인증시 일반적으로 비활성화)
                .csrf(csrf -> csrf.disable())

                // 세션 관리 정책 설정: STATELESS(상태 없음, 서버가 세션을 생성/유지하지 않음)
                // JWT 인증에선 세션이 필요없으므로 stateless 옵션 필수
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인가(Authorization) 규칙 정의
                .authorizeHttpRequests(auth -> auth
                        // 아래 경로들은 인증 없이 접근 허용(permitAll)
                        .requestMatchers(
                                "/api/v1/member/login",     // API 로그인
                                "/api/v1/member/join",      // API 회원가입
                                "/api/v1/jwt/auth",         // JWT 인증 관련 엔드포인트
                                "/member/**",               // 모든 /member 경로
                                "/",                        // 메인 페이지
                                "/css/*", "/js/*", "/images/**" // 정적 리소스
                        ).permitAll()
                        // 그 외의 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 커스텀 JWT 인증 필터를 UsernamePasswordAuthenticationFilter 이전에 등록
                // (폼 로그인 대신 JWT 토큰을 통한 인증을 수행하는 필터)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 최종적으로 SecurityFilterChain 객체를 반환(설정 완료)
        return http.build();
    }

    /**
     * AuthenticationManager 빈 등록
     * - Spring Security 5 이상에서 인증 매니저를 직접 주입해야 할 경우 사용
     * - AuthenticationConfiguration에서 AuthenticationManager를 받아서 반환
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}