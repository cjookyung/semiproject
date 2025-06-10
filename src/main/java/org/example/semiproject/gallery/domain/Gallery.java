package org.example.semiproject.gallery.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="gallerys")
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ggno;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String userid;

    @Column(nullable = false)
    private String simgname;

    @Column(nullable = false)
    private String contents;

    @CreationTimestamp
    private LocalDateTime regdate;
}
