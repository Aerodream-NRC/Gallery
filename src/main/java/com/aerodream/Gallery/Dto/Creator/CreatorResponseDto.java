package com.aerodream.Gallery.Dto.Creator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatorResponseDto {

    private Long userId;

    private Long creatorId;

    private Long subscribersCount;

    private Long collectionsCount;

    private boolean isReadyForOrder;
}
