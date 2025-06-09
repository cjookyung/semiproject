package org.example.semiproject.member.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.semiproject.member.domain.Member;
import org.example.semiproject.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        // 데이터베이스에서 userid로 회원 조회
        Member member = memberRepository.findByUserid(userid);

        // 사용자가 존재하지 않으면 예외 발생 (스프링 시큐리티가 인증 실패로 처리)
        if (member == null)
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userid);

        // 인증 절차에서 실제로 로그인한 사용자 정보 로그로 출력 (디버깅/모니터링용)
        log.info("로그인한 사용자 : {}", member);

        // UserDetails 객체 반환
        // - 스프링 시큐리티에서 사용하는 사용자 정보 객체
        // - 아이디, 비밀번호, 권한 정보를 전달 (여기서 권한은 ROLE_USER로 고정)
        return new CustomUserDetails(
                member.getUserid(),                                    // 사용자 아이디
                member.getPasswd(),                                    // 사용자 암호(암호화되어 있어야 함)
                member.getName(),
                member.getEmail(),
                member.getRegdate(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 권한(1개, ROLE_USER)
        );
    }

}
