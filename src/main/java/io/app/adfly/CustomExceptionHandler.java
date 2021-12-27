package io.app.adfly;

import com.fasterxml.jackson.databind.JsonSerializable;
import io.app.adfly.domain.dto.ErrorResponse;
import io.app.adfly.domain.exceptions.RecordNotFoundException;
import io.app.adfly.domain.exceptions.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import springfox.documentation.spring.web.json.JsonSerializer;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice(basePackages = {"io.app.adfly.controllers"})
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Record Not Found", details);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(ValidationException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (int i = 0; i < ex.getErrors().size(); i++) {
            details.add(ex.getErrors().get(i));
        }
        ;
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    }
}
