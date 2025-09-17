package com.aerodream.Gallery.Dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String login;

    private String email;

    public boolean hasLogin() {
        return login != null;
    }

    public boolean hasEmail() {
        return email != null;
    }
}
