package cz.cvut.fit.tjv.hospital_appointments.exception;

public class OneToOneRelationUpdateConflictException extends RuntimeException {

    public OneToOneRelationUpdateConflictException() {
        super("OneToOne relation between entities can not be added because it creates conflict");
    }
}
