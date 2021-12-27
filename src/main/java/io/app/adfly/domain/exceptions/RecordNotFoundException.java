package io.app.adfly.domain.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String exception) {
        super(exception);
    }
}
