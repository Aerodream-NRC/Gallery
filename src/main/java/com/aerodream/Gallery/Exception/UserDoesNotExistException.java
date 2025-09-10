package com.aerodream.Gallery.Exception;

import javax.naming.AuthenticationException;

public class UserDoesNotExistException extends AuthenticationException {

    public UserDoesNotExistException(final String message) {
        super(message);
    }
}
