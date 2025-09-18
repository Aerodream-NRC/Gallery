package com.aerodream.Gallery.Exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class CollectionNotFoundException extends ChangeSetPersister.NotFoundException {

    public CollectionNotFoundException(final String message) {
        super();
    }
}
