package org.example.semiproject.common.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.semiproject.member.domain.Member;
import org.example.semiproject.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtAuthController {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    /**
     * 인증 상태 단순 체크 API
     * - Authorization 헤더에서 JWT 추출 및 유효성 검증
     * - 올바른 JWT가 있으면 true, 아니면 false 반환
     */
    @GetMapping("/auth")
    public boolean myinfo(HttpServletRequest request) {
        boolean isAuth = false;

        // 1. Authorization 헤더에서 JWT 토큰 추출
        String header = request.getHeader("Authorization");
        String jwt = null;

        // 2. "Bearer "로 시작하면 JWT 추출
        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7); // "Bearer " 이후 토큰 부분만 추출
            // 3. 토큰 유효성 검사 (서명, 만료 등)
            if (jwtUtil.validateToken(jwt)) {
                isAuth = true; // 유효하면 인증 상태 true
            }
        }

        return isAuth; // 결과 반환
    }

    /**
     * 내 정보 조회 API (JWT 인증 필요)
     * - JWT가 올바르면 username 추출, DB에서 회원 정보 조회 후 반환
     * - 실패 시 401(인증 실패) 반환
     */
    @GetMapping("/fetchMyinfo")
    public ResponseEntity<?> fetchMyinfo(HttpServletRequest request) {
        // 1. 요청 헤더에서 JWT 추출
        String header = request.getHeader("Authorization");
        String jwt = null;

        // 2. "Bearer "로 시작하면 JWT 추출
        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);

            // 3. 토큰 유효성 검사
            if (jwtUtil.validateToken(jwt)) {
                // 4. 사용자 ID(username) 추출
                String userid = jwtUtil.getUsername(jwt);
                // 5. DB에서 사용자 정보 조회
                Member member = memberRepository.findByUserid(userid);
                if (member != null) {
                    // 6. 응답: 사용자 정보 반환 (단, password 등 민감정보는 @JsonIgnore 등으로 반드시 제외)
                    return ResponseEntity.ok(member);
                }
            }
        }

        // 인증 실패 또는 회원 정보 없음: 401 에러 반환
        return ResponseEntity.status(401).body("인증 실패");
    }


}
