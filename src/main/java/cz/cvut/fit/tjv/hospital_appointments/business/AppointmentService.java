package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.AppointmentJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService extends
        AbstractCrudService<Long, Appointment, AppointmentJpaRepository> {

    public AppointmentService(AppointmentJpaRepository repository) {
        super(repository);
    }

    @Override
    public Appointment update(Appointment appointment) {
        if (repository.existsById(appointment.getId())) {
            return repository.save(appointment);
        }
        throw new EntityNotFoundException(appointment);
    }
}
