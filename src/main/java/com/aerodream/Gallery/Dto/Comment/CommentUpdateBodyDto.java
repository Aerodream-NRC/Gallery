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
public class CommentUpdateBodyDto {

    private String commentBody;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
