package cz.cvut.fit.tjv.hospital_appointments.api.exception;

import cz.cvut.fit.tjv.hospital_appointments.exception.*;
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

    @ExceptionHandler(DeletingNonExistingEntityException.class)
    protected ResponseEntity<Object> handleDeletingNonExistingEntity(Exception e, WebRequest r) {
        return handleExceptionInternal(e, null, new HttpHeaders(), NO_CONTENT, r);
    }

    @ExceptionHandler(NonNullableFieldIsNullException.class)
    protected ResponseEntity<Object> handleNonNullableFieldIsNull(Exception e, WebRequest r) {
        String bodyOfResponse = "Non-Nullable field of entity can not be null";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), UNPROCESSABLE_ENTITY, r);
    }

    @ExceptionHandler(EndTimeBeforeStartTimeException.class)
    protected ResponseEntity<Object> handleEndTimeIsBeforeStartTime(Exception e, WebRequest r) {
        String bodyOfResponse = "End time must be after start time";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), UNPROCESSABLE_ENTITY, r);
    }

    @ExceptionHandler(DoctorAppointmentPatientCaseException.class)
    protected ResponseEntity<Object> handleDoctorAppointmentPatientCase(Exception e, WebRequest r) {
        String bodyOfResponse = "Doctor can have appointment for patient case only if he can work on this case";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), CONFLICT, r);
    }

    @ExceptionHandler(TimeIntervalsAreIntersectingException.class)
    protected ResponseEntity<Object> handleIntersectingTimeIntervals(Exception e, WebRequest r) {
        String bodyOfResponse = "Time intervals can not intersect";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), CONFLICT, r);
    }
}
