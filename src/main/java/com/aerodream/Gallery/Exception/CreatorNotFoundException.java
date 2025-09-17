package com.aerodream.Gallery.Exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class CreatorNotFoundException extends ChangeSetPersister.NotFoundException {

    public CreatorNotFoundException(final String message) {
        super();
    }
}
