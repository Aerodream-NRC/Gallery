package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "likes")
public class LikeEntity {
    @EmbeddedId
    private LikeId id;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artworkId")
    @JoinColumn(name = "artwork_id",
            insertable = false,
            updatable = false)
    private ArtworkEntity artwork;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id",
            insertable = false,
            updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    public LikeEntity(UserEntity user, ArtworkEntity artwork) {
        this.user = user;
        this.artwork = artwork;
        this.id = new LikeId(user.getId(), artwork.getId());
    }

    public LikeEntity(UserEntity user, CommentEntity comment) {
        this.user = user;
        this.comment = comment;
        this.id = new LikeId(user.getId(), comment.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeEntity that = (LikeEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

