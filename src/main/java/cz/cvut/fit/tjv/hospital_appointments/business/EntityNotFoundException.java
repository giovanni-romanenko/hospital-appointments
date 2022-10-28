package cz.cvut.fit.tjv.hospital_appointments.business;

public class EntityNotFoundException extends RuntimeException {

    public <E> EntityNotFoundException(E entity) {
        super("Entity not found: " + entity);
    }
}
