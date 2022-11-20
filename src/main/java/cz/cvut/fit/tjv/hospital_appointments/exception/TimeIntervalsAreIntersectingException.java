package cz.cvut.fit.tjv.hospital_appointments.exception;

public class TimeIntervalsAreIntersectingException extends RuntimeException {

    public TimeIntervalsAreIntersectingException() {
        super("Time intervals are intersecting");
    }
}
