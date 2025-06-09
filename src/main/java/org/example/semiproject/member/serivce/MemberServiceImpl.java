package org.example.semiproject.member.serivce;

import lombok.RequiredArgsConstructor;
import org.example.semiproject.member.domain.Member;
import org.example.semiproject.member.domain.dto.LoginDTO;
import org.example.semiproject.member.domain.dto.MemberDTO;
import org.example.semiproject.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberMapper;

    public boolean newMember(MemberDTO member) {

        // 아이디 중복 체크
        if (memberMapper.countByUserid(member.getUserid()) > 0) {
            throw new IllegalStateException("이미 존재하는 아이디입니다!!");
        }

        // 이메일 중복 체크
        if (memberMapper.countByEmail(member.getEmail()) > 0) {
            throw new IllegalStateException("이미 존재하는 이메일입니다!!");
        }

        int result = memberMapper.insertMember(member);
        return result == 1;  // 회원정보가 테이블 저장되었는지 여부에 따라
        // true/false 반환
    }

    public Member loginMember(LoginDTO member) {
        Member findMember = memberMapper.findByUserid(member.getUserid());

        if (findMember == null || !findMember.getPasswd().equals(member.getPasswd())) {
            throw new IllegalStateException("아이디나 비밀번호가 일치하지 않습니다!!");
        }

        return findMember;
    }

}
