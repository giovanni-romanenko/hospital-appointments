package cz.cvut.fit.tjv.hospital_appointments.exception;

public class CreatedEntityNullIdException extends RuntimeException {

    public <E> CreatedEntityNullIdException(E entity) {
        super("Created entity has null id: " + entity);
    }
}
