package cz.cvut.fit.tjv.hospital_appointments.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Entity not found");
    }

    public <E> EntityNotFoundException(E entity) {
        super("Entity not found: " + entity);
    }
}
