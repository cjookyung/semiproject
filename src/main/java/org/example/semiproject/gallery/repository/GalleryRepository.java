package org.example.semiproject.gallery.repository;

import org.example.semiproject.gallery.domain.Gallery;
import org.example.semiproject.gallery.domain.dto.GalleryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    @Query(value = "select ggno, title, userid, regdate, simgname from gallerys order by ggno desc", nativeQuery = true)
    List<GalleryDTO> selectGallery();
}