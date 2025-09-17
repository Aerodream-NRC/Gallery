package com.aerodream.Gallery.Dto.Artwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkResponseDto {

    private Long id;

    private String title;

    private String description;

    private String imageS3Key;

    private int likeCount;

    private int commentCount;

    private boolean isHiddenComments;

    private boolean isSold;

    private LocalDateTime createdAt;
}
