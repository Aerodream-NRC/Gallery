package com.aerodream.Gallery.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class LikeId implements Serializable {

    @Column(name = "user_id")
    private long userId;

    @Column(name = "artwork_id")
    private long artworkId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return userId == likeId.userId &&
                artworkId == likeId.artworkId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId,
                artworkId);
    }
}
