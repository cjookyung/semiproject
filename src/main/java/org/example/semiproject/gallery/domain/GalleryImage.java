package org.example.semiproject.gallery.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name="gallery_images")
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class GalleryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int gno;

    @Column(nullable = false)
    private String imgname;

    @Column(nullable = false)
    private int imgsize;
}
