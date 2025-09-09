package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "artworks")
public class ArtworkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(
            name = "image_s3_key",
            nullable = false,
            unique = true
    )
    private String imageS3Key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private CreatorEntity creator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "artwork_tags",
            joinColumns = @JoinColumn(name = "artwork_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    @OneToMany(
            mappedBy = "artwork",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CommentEntity> comments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private CollectionEntity collection;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_hidden_comments")
    private boolean isHiddenComments = false;

    @Column(name = "is_sold")
    private boolean isSold = false;

    public ArtworkEntity(
            String title,
            String description,
            String imageS3Key,
            CreatorEntity creator) {
        this.title = title;
        this.description = description;
        this.imageS3Key = imageS3Key;
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtworkEntity that = (ArtworkEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addComment(CommentEntity comment) {
        comments.add(comment);
        comment.setArtwork(this);
    }

    public void removeComment(CommentEntity comment) {
        comments.remove(comment);
        comment.setArtwork(null);
    }
}
