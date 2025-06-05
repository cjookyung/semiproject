package com.example.semiproject.member.service;

import com.example.semiproject.member.domain.dto.LoginDTO;
import com.example.semiproject.member.domain.dto.MemberDTO;
import com.example.semiproject.member.domain.Member;

public interface MemberService {
    boolean newMember(MemberDTO member);
    Member loginMember(LoginDTO member);
}
