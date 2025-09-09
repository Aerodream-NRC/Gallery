package com.aerodream.Gallery.Exception;

import javax.naming.AuthenticationException;

public class UserAlreadyExistException extends AuthenticationException {

    public UserAlreadyExistException(final String message) {
        super (message);
    }
}
