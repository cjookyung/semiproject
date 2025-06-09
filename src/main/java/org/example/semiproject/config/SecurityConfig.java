package org.example.semiproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF(Cross-Site Request Forgery) 보호 기능 비활성화
                // REST API, 혹은 CSRF 토큰을 사용하지 않는 경우 disable
                .csrf(csrf -> csrf.disable())

                // 요청 인가(Authorization) 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        // 지정된 경로들에는 누구나 접근 허용 (회원가입, 로그인, 정적자원 등)
                        .requestMatchers(
                                "/",                                 // 메인 페이지
                                "/member/join", "/member/login",     // 회원가입, 로그인 페이지
                                "/api/v1/member/join",               // API 회원가입
                                "/api/v1/member/login",              // API 로그인
                                "/css/**", "/js/**", "/images/**"    // 정적 리소스(css, js, 이미지)
                        ).permitAll()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 폼 로그인(Form Login) 설정
                .formLogin(form -> form
                        .loginPage("/member/login")         // 사용자 정의 로그인 페이지 경로
                        .loginProcessingUrl("/member/login")// 로그인 form action URL (Spring이 처리)
                        .usernameParameter("userid")        // 로그인 form의 아이디 필드 name
                        .passwordParameter("passwd")        // 로그인 form의 비밀번호 필드 name
                        .defaultSuccessUrl("/", true)       // 로그인 성공 시 이동할 경로 (항상 메인으로 이동)
                        .failureUrl("/member/login?error")  // 로그인 실패 시 이동할 경로
                        .permitAll()                        // 로그인 관련 경로는 모두 접근 허용
                )

                // 로그아웃(Logout) 설정
                .logout(logout -> logout
                        .logoutUrl("/member/logout")        // 로그아웃 요청 경로
                        .logoutSuccessUrl("/")              // 로그아웃 성공 후 리다이렉트 경로
                        .invalidateHttpSession(true)        // 로그아웃 시 세션 무효화
                        .deleteCookies("JSESSIONID")        // JSESSIONID 쿠키 삭제
                );
        // 최종적으로 SecurityFilterChain 객체 반환 (설정 완료)
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}