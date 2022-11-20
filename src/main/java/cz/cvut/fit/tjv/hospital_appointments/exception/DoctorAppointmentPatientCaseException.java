package cz.cvut.fit.tjv.hospital_appointments.exception;

public class DoctorAppointmentPatientCaseException extends RuntimeException {

    public DoctorAppointmentPatientCaseException() {
        super("Doctor can have Appointment with Patient Case only if Doctor can work on this Patient Case");
    }
}
