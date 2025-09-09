package com.aerodream.Gallery.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class LikeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "artwork_id")
    private Long artworkId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(userId, likeId.userId) &&
                Objects.equals(artworkId, likeId.artworkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId,
                artworkId);
    }
}
