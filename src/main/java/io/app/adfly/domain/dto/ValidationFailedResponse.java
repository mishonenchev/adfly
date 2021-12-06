package io.app.adfly.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class ValidationFailedResponse {
    public ValidationFailedResponse(ValidationError error){
        errors = new HashSet<ValidationError>();
        errors.add(error);
    }

    public ValidationFailedResponse(Set<ValidationError> errors){
        setErrors(errors);
    }

    public Set<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(Set<ValidationError> errors) {
        this.errors = errors;
    }

    private Set<ValidationError> errors;

}
