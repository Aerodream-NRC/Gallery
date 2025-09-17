package com.aerodream.Gallery.Dto.User;

import com.aerodream.Gallery.Enum.RoleEnum;
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
public class UserResponseDto {

    private Long id;

    private String login;

    private String email;

    private Set<RoleEnum> roles;

    private LocalDateTime createdAt;

    private Long creatorId;
}