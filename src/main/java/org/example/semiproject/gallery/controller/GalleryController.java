package org.example.semiproject.gallery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.semiproject.gallery.domain.Gallery;
import org.example.semiproject.gallery.service.GalleryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryController {
    private final GalleryService galleryService;

    @Value("${recaptcha_sitekey}")
    private String sitekey;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("gals", galleryService.readGallery());

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