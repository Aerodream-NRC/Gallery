package com.aerodream.Gallery.Dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    private Long artworkId;

    private Long userId;

    private LocalDateTime createdAt;

    private String commentBody;

    private boolean isHidden;

    private boolean isLikedByCreator;
}
