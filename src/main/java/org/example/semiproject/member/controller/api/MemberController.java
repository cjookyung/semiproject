package org.example.semiproject.member.controller.api;

import jakarta.servlet.http.HttpSession;
import org.example.semiproject.member.domain.Member;
import org.example.semiproject.member.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Value("${recaptcha_sitekey}")
    private String sitekey;

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("sitekey", sitekey);

        return "views/member/join";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("sitekey", sitekey);

        return "views/member/login";
    }

    @GetMapping("/myinfo")
    public String myinfo(Model model, Authentication authentication) {
        String returnPage = "redirect:/member/login";

        if (authentication != null && authentication.isAuthenticated()) {
            // UserDetails에서 아이디 등 정보 추출
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            model.addAttribute("loginUser", user);
            returnPage = "views/member/myinfo";
        }

        return returnPage;
    }

    //security가 로그아웃 처리해 줌!!
//    @GetMapping("/logout")
//    public String logout() {
//
//        return "redirect:/member/logout";
//    }

}
