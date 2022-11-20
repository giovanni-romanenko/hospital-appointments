package cz.cvut.fit.tjv.hospital_appointments.exception;

public class NonNullableFieldIsNullException extends RuntimeException {

    public NonNullableFieldIsNullException() {
        super("Non nullable field is null");
    }
}
