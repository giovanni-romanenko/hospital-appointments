package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.DoctorJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DoctorService extends
        AbstractCrudService<Long, Doctor, DoctorJpaRepository> {

    public DoctorService(DoctorJpaRepository repository) {
        super(repository);
    }

    @Override
    public Doctor update(Doctor doctor) {
        if (repository.existsById(doctor.getId())) {
            return repository.save(doctor);
        }
        throw new EntityNotFoundException(doctor);
    }
}
