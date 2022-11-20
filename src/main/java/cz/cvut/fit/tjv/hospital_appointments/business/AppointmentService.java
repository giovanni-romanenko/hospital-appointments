package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.AppointmentJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.exception.EndTimeBeforeStartTimeException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import cz.cvut.fit.tjv.hospital_appointments.exception.NonNullableFieldIsNullException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService extends
        AbstractCrudService<Long, Appointment, AppointmentJpaRepository> {

    public AppointmentService(AppointmentJpaRepository repository) {
        super(repository);
    }

    @Override
    public Appointment update(Appointment appointment) {
        checkNonNullableValues(appointment);
        checkEntityIntegrityConstraints(appointment);
        Appointment existingAppointment = readById(appointment.getId()).orElseThrow(EntityNotFoundException::new);
        appointment.setDoctor(existingAppointment.getDoctor());
        appointment.setPatientCase(existingAppointment.getPatientCase());
        return repository.save(appointment);
    }

    @Transactional
    public void deleteDoctorOfAppointment(@NonNull Long appointmentId) {
        Appointment appointment = readById(appointmentId).orElseThrow(EntityNotFoundException::new);
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
}
