package org.example.semiproject.member.serivce;

import org.example.semiproject.member.domain.Member;
import org.example.semiproject.member.domain.dto.LoginDTO;
import org.example.semiproject.member.domain.dto.MemberDTO;

public interface MemberService {

    boolean newMember(MemberDTO member);
    Member loginMember(LoginDTO member);

}