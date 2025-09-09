package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "collections")
public class CollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private CreatorEntity creator;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "collection")
    private Set<ArtworkEntity> artworks = new HashSet<>();

    public CollectionEntity(String title,
                            String description,
                            CreatorEntity creator) {
        this.title = title;
        this.description = description;
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionEntity that = (CollectionEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addArtwork(ArtworkEntity artwork) {
        artworks.add(artwork);
        artwork.setCollection(this);
    }

    public void removeArtwork(ArtworkEntity artwork) {
        artworks.remove(artwork);
        artwork.setCollection(null);
    }
}
