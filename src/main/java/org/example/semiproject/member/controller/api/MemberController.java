package org.example.semiproject.member.controller.api;

import jakarta.servlet.http.HttpSession;
import org.example.semiproject.member.domain.Member;
import org.springframework.beans.factory.annotation.Value;
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
    public String myinfo(Model model, HttpSession session) {
        String returnPage = "redirect:/member/login";

        //Member user = new Member(0,"abc123","abc123","abc123",
        //        "abc123@abc123.co.kr", LocalDateTime.now());
        //model.addAttribute("loginUser", user);

        // 세션변수가 생성되어 있다면 myinfo로 이동가능
        if (session.getAttribute("loginUser") != null) {
            model.addAttribute("loginUser", session.getAttribute("loginUser"));
            returnPage = "views/member/myinfo";
        }

        return returnPage;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/";
    }

}
