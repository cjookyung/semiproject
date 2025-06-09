package org.example.semiproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 해당 클래스가 스프링의 설정 클래스임을 명시
@EnableWebSecurity // Spring Security를 활성화시키는 어노테이션
@RequiredArgsConstructor // final이나 @NonNull이 붙은 필드에 대해 생성자 자동 생성
public class SecurityConfig {

    /**
     * SecurityFilterChain 빈 등록
     * - 스프링 시큐리티의 각종 보안 규칙(인증, 인가 등)을 필터 체인 형태로 적용
     * - HttpSecurity를 통해 다양한 보안 설정을 추가할 수 있음
     *
     * @param http HttpSecurity 객체(Spring Security의 보안 설정 API)
     * @return SecurityFilterChain 객체 (설정된 보안 필터 체인)
     * @throws Exception 예외 발생 시
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (API용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/board/list").hasRole("ADMIN") // 관리자 권한 필요
                        .requestMatchers("/member/logout").hasRole("USER") // 인증 필요
                        .anyRequest().permitAll() // 그 외는 모두 허용
                )
                .httpBasic(httpBasic -> httpBasic.realmName("HttpBasic")); // HTTP Basic 인증 활성화
        return http.build();
    }

    // UserDetailsService 빈 등록: 사용자 정보를 메모리에서 관리하는 서비스
    @Bean
    public UserDetailsService userDetailsService() {
        // 일반 사용자 계정 생성: username = "user", password = "user123", 권한 = "USER"
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123")) // 비밀번호는 암호화 필요
                .roles("USER")
                .build();

        // 관리자 계정 생성: username = "admin", password = "admin123", 권한 = "USER", "ADMIN"
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("USER", "ADMIN")
                .build();

        // 위에서 만든 두 계정 정보를 InMemoryUserDetailsManager에 등록 (메모리 기반 사용자 관리)
        return new InMemoryUserDetailsManager(user, admin);
    }

    // PasswordEncoder 빈 등록: 비밀번호를 암호화하기 위한 인코더(Bcrypt 알고리즘 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
