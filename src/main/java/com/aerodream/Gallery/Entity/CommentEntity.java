package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id")
    private ArtworkEntity artwork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDateTime createdAt;

    private String commentBody;

    private boolean isHidden;
}
