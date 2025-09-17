package com.aerodream.Gallery.Dto.Artwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkUpdateDto {

    private String title;

    private String description;

    private Long collectionId;

//    private Set<> tags;

    private boolean isHiddenComments;

    private boolean isSold;

    public boolean hasTitle() {
        return title != null;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasCollectionId() {
        return collectionId != null;
    }

//    public boolean hasTags() {
//        return tags != null;
//    }
}
