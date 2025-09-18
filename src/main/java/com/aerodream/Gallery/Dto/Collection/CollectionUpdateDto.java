package com.aerodream.Gallery.Dto.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionUpdateDto {

    private String title;

    private String description;

    private Set<Long> artworksId;

    LocalDateTime updatedAt = LocalDateTime.now();

    public boolean hasTitle() {
        return title != null;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasArtworksId() {
        return artworksId != null;
    }
}
