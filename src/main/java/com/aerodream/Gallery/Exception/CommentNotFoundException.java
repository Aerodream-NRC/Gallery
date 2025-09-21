package com.aerodream.Gallery.Exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class CommentNotFoundException extends ChangeSetPersister.NotFoundException {

    public CommentNotFoundException(final String message) {
        super();
    }
}