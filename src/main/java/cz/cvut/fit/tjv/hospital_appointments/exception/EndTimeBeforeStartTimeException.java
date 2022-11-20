package cz.cvut.fit.tjv.hospital_appointments.exception;

public class EndTimeBeforeStartTimeException extends RuntimeException {

    public EndTimeBeforeStartTimeException() {
        super("Start time must be before end time");
    }
}
