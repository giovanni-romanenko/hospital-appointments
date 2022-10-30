package cz.cvut.fit.tjv.hospital_appointments.api.exception;

import cz.cvut.fit.tjv.hospital_appointments.exception.CreatedEntityNullIdException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import cz.cvut.fit.tjv.hospital_appointments.exception.OneToOneRelationUpdateConflictException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(Exception e, WebRequest r) {
        String bodyOfResponse = "Entity not found";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), NOT_FOUND, r);
    }

    @ExceptionHandler(CreatedEntityNullIdException.class)
    protected ResponseEntity<Object> handleCreatedEntityNullId(Exception e, WebRequest r) {
        String bodyOfResponse = "Server Internal Error: Created entity has null id";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), INTERNAL_SERVER_ERROR, r);
    }

    @ExceptionHandler(OneToOneRelationUpdateConflictException.class)
    protected ResponseEntity<Object> handleOneToOneRelationUpdateConflict(Exception e, WebRequest r) {
        String bodyOfResponse = "One of One-to-One relation entities is already in relation with another entity";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), CONFLICT, r);
    }
}
