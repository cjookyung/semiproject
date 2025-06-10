package org.example.semiproject.gallery.domain.dto;

import java.time.LocalDateTime;

public interface GalleryDTO {
    int getGgno();
    String getTitle();
    String getUserid();
    LocalDateTime getRegdate();
    String getSimgname();
    String getThumbs();
    String getViews();
}