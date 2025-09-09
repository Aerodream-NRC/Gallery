package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private CreatorEntity creator;

    private LocalDateTime createdAt;
}
