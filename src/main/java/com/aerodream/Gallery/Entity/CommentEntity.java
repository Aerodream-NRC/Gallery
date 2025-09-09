package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "comment_body")
    private String commentBody;

    @Column(name = "is_hidden")
    private boolean isHidden;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return id == that.id &&
                isHidden == that.isHidden &&
                Objects.equals(artwork, that.artwork) &&
                Objects.equals(user, that.user) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(commentBody, that.commentBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                artwork,
                user,
                createdAt,
                commentBody,
                isHidden);
    }
}
