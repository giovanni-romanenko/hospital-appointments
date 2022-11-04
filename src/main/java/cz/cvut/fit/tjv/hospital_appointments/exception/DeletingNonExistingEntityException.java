package cz.cvut.fit.tjv.hospital_appointments.exception;

public class DeletingNonExistingEntityException extends RuntimeException {

    public DeletingNonExistingEntityException() {
        super("Attempt to delete non-existing entity");
    }
}
