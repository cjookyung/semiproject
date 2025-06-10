package org.example.semiproject.gallery.repository;

import org.example.semiproject.gallery.domain.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {

}