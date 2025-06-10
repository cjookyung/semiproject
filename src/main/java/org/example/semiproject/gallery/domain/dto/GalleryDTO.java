package org.example.semiproject.gallery.domain.dto;

import java.time.LocalDateTime;

public interface GalleryDTO {
    int getBno();
    String getTitle();
    String getUserid();
    LocalDateTime getRegdate();
    String getSImagName();
    String getThumbs();
    String getViews();
}