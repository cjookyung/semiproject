package org.example.semiproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/security/users")
public class InMemoryUserDetailsController {

    // UserDetailsService를 의존성 주입(Autowired) 받음
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 특정 사용자의 상세 정보 조회
     * @param username 조회할 사용자 이름 (URL 경로 변수로 전달)
     * @return 사용자 정보(맵 형태). 사용자가 없으면 에러 메시지 반환.
     */
    @GetMapping("/{username}")
    public Map<String, Object> getUserInfo(@PathVariable String username) {
        try {
            // 사용자 이름으로 UserDetails 객체 조회
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 사용자 정보를 Map으로 정리
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", userDetails.getUsername()); // 아이디
            userInfo.put("authorities", userDetails.getAuthorities()); // 권한 목록
            userInfo.put("enabled", userDetails.isEnabled()); // 활성화 여부
            userInfo.put("accountNonExpired", userDetails.isAccountNonExpired()); // 계정 만료 여부
            userInfo.put("accountNonLocked", userDetails.isAccountNonLocked()); // 계정 잠금 여부
            userInfo.put("credentialsNonExpired", userDetails.isCredentialsNonExpired()); // 비밀번호 만료 여부
            return userInfo;
        } catch (UsernameNotFoundException e) {
            // 사용자를 찾지 못한 경우 에러 메시지 반환
            Map<String, Object> error = new HashMap<>();
            error.put("error", "사용자를 찾을 수 없습니다: " + username);
            return error;
        }
    }

    /**
     * 사용자 존재 여부 확인
     * @param username 검사할 사용자 이름 (URL 경로 변수)
     * @return 사용자 존재 여부 (exists: true/false)
     */
    @GetMapping("/exists/{username}")
    public Map<String, Boolean> userExists(@PathVariable String username) {
        // InMemoryUserDetailsManager로 캐스팅 (해당 객체만 userExists 메서드 제공)
        InMemoryUserDetailsManager manager = (InMemoryUserDetailsManager) userDetailsService;
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", manager.userExists(username)); // 사용자 존재 여부 확인
        return result;
    }

    /**
     * 모든 사용자 목록 조회
     * InMemoryUserDetailsManager는 직접 사용자 리스트를 반환하는 메서드를 제공하지 않으므로,
     * 등록된 사용자명을 알고 있다면 해당 사용자명으로 정보를 조회해서 목록을 만듦.
     * @return 모든 사용자 정보 리스트와 총 사용자 수
     */
    @GetMapping("/all")
    public Map<String, Object> getAllUsers() {
        // 반환 결과를 담을 Map 생성
        Map<String, Object> response = new HashMap<>();

        // 등록된 사용자명 리스트 (InMemoryUserDetailsManager에 실제 등록된 사용자명 기입)
        List<String> usernames = Arrays.asList("user", "admin");

        List<Map<String, Object>> users = new ArrayList<>();
        // 각 사용자명에 대해 사용자 정보 조회
        for (String username : usernames) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", userDetails.getUsername());
                userInfo.put("authorities", userDetails.getAuthorities());
                userInfo.put("enabled", userDetails.isEnabled());
                users.add(userInfo); // 사용자 정보 리스트에 추가
            } catch (UsernameNotFoundException e) {
                // 사용자가 없는 경우는 건너뜀
            }
        }

        response.put("users", users); // 사용자 정보 리스트
        response.put("totalCount", users.size()); // 사용자 총 인원 수
        return response;
    }

}