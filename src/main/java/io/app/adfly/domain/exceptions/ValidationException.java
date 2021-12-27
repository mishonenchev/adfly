package io.app.adfly.domain.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {
    public ValidationException(List<String> errors){
        this.errors = errors;
    }
    public ValidationException(String error){
        this.errors = new ArrayList<>();
        this.errors.add(error);

    }

    public List<String> getErrors() {
        return errors;
    }

    private List<String> errors;


}
