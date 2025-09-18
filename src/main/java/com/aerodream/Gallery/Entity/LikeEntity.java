package com.aerodream.Gallery.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artworkId")
    @JoinColumn(name = "artwork_id",
            insertable = false,
            updatable = false)
    private Long artworkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id",
            insertable = false,
            updatable = false)
    private Long userId;

    public LikeEntity(Long userId, Long artworkId) {
        this.userId = userId;
        this.artworkId = artworkId;
        this.id = new LikeId(userId, artworkId);
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

