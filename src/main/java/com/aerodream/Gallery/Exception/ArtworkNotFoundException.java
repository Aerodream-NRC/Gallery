package com.aerodream.Gallery.Exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class ArtworkNotFoundException extends ChangeSetPersister.NotFoundException {

    public ArtworkNotFoundException(final String message) {
        super();
    }
}
