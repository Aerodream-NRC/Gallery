package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id")
    private ArtworkEntity artwork;
}