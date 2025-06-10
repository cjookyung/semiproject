package org.example.semiproject.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.semiproject.common.jwt.JwtUtil;
import org.example.semiproject.member.security.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter: 요청당 단 한번만 작동하는 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. HTTP 요청 헤더에서 Authorization 값을 가져옴
        String header = request.getHeader("Authorization");
        String token = null;      // JWT 토큰 변수
        String username = null;   // 토큰에서 추출할 사용자명

        // 2. Authorization 헤더가 "Bearer "로 시작하면 JWT 토큰임을 의미
        if (header != null && header.startsWith("Bearer ")) {
            // "Bearer " 다음의 JWT 토큰만 추출
            token = header.substring(7);

            // 2-1. 토큰 유효성 검사 (서명, 만료 등)
            if (jwtUtil.validateToken(token)) {
                // 2-2. 토큰에서 사용자명(username) 추출
                username = jwtUtil.getUsername(token);
            }
        }

        // 3. 인증 객체가 없는 상태에서 username이 정상적으로 추출 되었으면 로그인 처리
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 3-1. 사용자 정보 로드 (DB/메모리 등에서)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 3-2. 인증 객체 생성 (비밀번호는 null, 권한은 userDetails에서 꺼냄)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 3-3. 현재 요청의 SecurityContext에 인증 객체 등록 (로그인 처리 효과)
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 4. 다음 필터로 요청 전달 (필수)
        filterChain.doFilter(request, response);
    }

}