package cz.cvut.fit.tjv.hospital_appointments.api.exception;

import cz.cvut.fit.tjv.hospital_appointments.business.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(Exception e, WebRequest r) {
        String bodyOfResponse = "Entity not found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), NOT_FOUND, r);
    }
}
