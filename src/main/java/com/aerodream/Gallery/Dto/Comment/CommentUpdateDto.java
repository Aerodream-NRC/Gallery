package com.aerodream.Gallery.Dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {

    private boolean isHidden;

    private boolean isLikedByCreator;
}
