package cz.cvut.fit.tjv.hospital_appointments.exception;

public class EntityNotFoundException extends RuntimeException {

    public <E> EntityNotFoundException(E entity) {
        super("Entity not found: " + entity);
    }
}
