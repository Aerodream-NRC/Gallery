package com.aerodream.Gallery.Exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class UserNotFoundException extends ChangeSetPersister.NotFoundException {

    public UserNotFoundException(final String message) {
        super();
    }
}
