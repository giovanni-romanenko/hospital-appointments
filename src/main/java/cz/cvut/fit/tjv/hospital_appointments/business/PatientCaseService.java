package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.PatientCaseJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import org.springframework.stereotype.Service;

@Service
public class PatientCaseService extends
        AbstractCrudService<Long, PatientCase, PatientCaseJpaRepository> {

    public PatientCaseService(PatientCaseJpaRepository repository) {
        super(repository);
    }

    @Override
    public PatientCase update(PatientCase doctor) {
        if (repository.existsById(doctor.getId())) {
            return repository.save(doctor);
        }
        throw new EntityNotFoundException(doctor);
    }
}
