package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.AppointmentJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.exception.EndTimeBeforeStartTimeException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import cz.cvut.fit.tjv.hospital_appointments.exception.NonNullableFieldIsNullException;
import cz.cvut.fit.tjv.hospital_appointments.exception.TimeIntervalsAreIntersectingException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AppointmentService extends
        AbstractCrudService<Long, Appointment, AppointmentJpaRepository> {

    private final DoctorService doctorService;

    public AppointmentService(AppointmentJpaRepository repository, DoctorService doctorService) {
        super(repository);
        this.doctorService = doctorService;
    }

    @Override
    public Appointment update(Appointment appointment) {
        Appointment existingAppointment = readById(appointment.getId()).orElseThrow(EntityNotFoundException::new);
        checkNonNullableValues(appointment);
        checkEntityIntegrityConstraints(appointment);
        if (existingAppointment.getDoctor() != null) {
            checkDoctorAppointmentsTimesAreNotIntersecting(existingAppointment.getDoctor(), appointment);
        }
        appointment.setDoctor(existingAppointment.getDoctor());
        appointment.setPatientCase(existingAppointment.getPatientCase());
        return repository.save(appointment);
    }

    @Transactional
    public void updateDoctorOfAppointment(@NonNull Long appId, @NonNull Long docId) {
        Appointment appointment = readById(appId).orElseThrow(EntityNotFoundException::new);
        Doctor doctor = doctorService.readById(docId).orElseThrow(EntityNotFoundException::new);
        if (!doctor.getAppointments().contains(appointment)) {
            checkDoctorAppointmentsTimesAreNotIntersecting(doctor, appointment);
        }
        appointment.setDoctor(doctor);
        doctorService.checkDoctorsOnlyHaveAppointmentsForPatientCasesWhichTheyCanWorkOn();
    }

    @Transactional
    public void deleteDoctorOfAppointment(@NonNull Long appId) {
        Appointment appointment = readById(appId).orElseThrow(EntityNotFoundException::new);
        appointment.setDoctor(null);
    }

    @Override
    protected void checkNonNullableValues(Appointment appointment) {
        if (appointment.getToTime() == null || appointment.getFromTime() == null) {
            throw new NonNullableFieldIsNullException();
        }
    }

    @Override
    protected void checkEntityIntegrityConstraints(Appointment appointment) {
        if (!appointment.getFromTime().isBefore(appointment.getToTime())) {
            throw new EndTimeBeforeStartTimeException();
        }
    }

    private void checkDoctorAppointmentsTimesAreNotIntersecting(Doctor doctor, Appointment appointment) {
        Set<Appointment> doctorAppointments = doctor.getAppointments();
        doctorAppointments.forEach(doctorAppointment -> {
            if (!doctorAppointment.getId().equals(appointment.getId())
                    && appointment.getToTime().isAfter(doctorAppointment.getFromTime())
                    && doctorAppointment.getToTime().isAfter(appointment.getFromTime())) {
                throw new TimeIntervalsAreIntersectingException();
            }
        });
    }
}
