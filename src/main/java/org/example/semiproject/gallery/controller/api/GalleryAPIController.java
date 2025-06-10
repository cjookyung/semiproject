package org.example.semiproject.gallery.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.semiproject.common.utils.GoogleRecaptchaService;
import org.example.semiproject.gallery.domain.Gallery;
import org.example.semiproject.gallery.service.GalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/gallery")
public class GalleryAPIController {

    private final GalleryService galleryService;
    private final GoogleRecaptchaService googleRecaptchaService;

    @PostMapping("/write")
    public ResponseEntity<?> writeok(Gallery gal, List<MultipartFile> ginames,
                                     @RequestParam("g-recaptcha-response") String gRecaptchaResponse) {
        ResponseEntity<?> response = ResponseEntity.internalServerError().build();
        log.info("submit된 갤러리 정보1 : {}" , gal);
        log.info("submit된 갤러리 정보2 : {}" , ginames);

        try {
            if (!googleRecaptchaService.verifyRecaptcha(gRecaptchaResponse)) {
                throw new IllegalStateException("자동가입방지 코드 오류!!");
            }

            if (galleryService.newGalleryImage(gal, ginames)) {
                response = ResponseEntity.ok().build();
            }
        } catch (IllegalStateException ex) {
            response = ResponseEntity.badRequest().body(ex.getMessage());
        }

        return response;
    }
}
