package org.example.semiproject.gallery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/gallery")
public class GalleryController {

    @Value("${recaptcha_sitekey}")
    private String sitekey;

    @GetMapping("/list")
    public String list(Model model) {

        return "views/gallery/list";
    }

    @GetMapping("/view/{gno}")
    public String view(Model model) {

        return "views/gallery/view";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("sitekey", sitekey);

        return "views/gallery/write";
    }
}