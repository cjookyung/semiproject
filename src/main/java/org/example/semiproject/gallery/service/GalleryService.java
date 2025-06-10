package org.example.semiproject.gallery.service;


import org.example.semiproject.gallery.domain.Gallery;
import org.example.semiproject.gallery.domain.dto.GalleryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GalleryService {

    boolean newGalleryImage(Gallery gal, List<MultipartFile> ginames);
    List<GalleryDTO> readGallery();
}