package org.example.semiproject.gallery.repository;

import org.example.semiproject.gallery.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

}