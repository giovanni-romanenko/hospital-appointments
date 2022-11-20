package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.PatientCaseJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import cz.cvut.fit.tjv.hospital_appointments.exception.DeletingNonExistingEntityException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import cz.cvut.fit.tjv.hospital_appointments.exception.OneToOneRelationUpdateConflictException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class PatientCaseService extends
        AbstractCrudService<Long, PatientCase, PatientCaseJpaRepository> {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public PatientCaseService(PatientCaseJpaRepository repository, AppointmentService appointmentService,
                              DoctorService doctorService) {
        super(repository);
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @Override
    public PatientCase update(PatientCase patientCase) {
        checkNonNullableValues(patientCase);
        PatientCase existingPatientCase = readById(patientCase.getId()).orElseThrow(EntityNotFoundException::new);
        patientCase.setQualifiedDoctors(existingPatientCase.getQualifiedDoctors());
        return repository.save(patientCase);
    }

    @Override
    protected void deleteRelationsById(Long id) {
        PatientCase patientCase = readById(id).orElseThrow(DeletingNonExistingEntityException::new);
        Appointment appointment = patientCase.getAppointment();
        if (appointment != null) {
            appointment.setPatientCase(null);
        }
    }

    @Transactional
    public Collection<Doctor> readAllDoctorsWhoCanWorkOnPatientCase(@NonNull Long caseId) {
        PatientCase patientCase = readById(caseId).orElseThrow(EntityNotFoundException::new);
        return patientCase.getQualifiedDoctors();
    }

    @Transactional
    public void updateAppointmentOfPatientCase(@NonNull Long caseId, @NonNull Long appId) {
        PatientCase patientCase = readById(caseId).orElseThrow(EntityNotFoundException::new);
        Appointment appointment = appointmentService.readById(appId).orElseThrow(EntityNotFoundException::new);
        if (patientCase.getAppointment() == null && appointment.getPatientCase() == null) {
            appointment.setPatientCase(patientCase);
            doctorService.checkDoctorsOnlyHaveAppointmentsForPatientCasesWhichTheyCanWorkOn();
        }
        else if (!(appointment.equals(patientCase.getAppointment()) && patientCase.equals(appointment.getPatientCase()))) {
            throw new OneToOneRelationUpdateConflictException();
        }
    }

    @Transactional
    public void deleteAppointmentOfPatientCase(@NonNull Long caseId, @NonNull Long appId) {
        PatientCase patientCase = readById(caseId).orElseThrow(EntityNotFoundException::new);
        Appointment appointment = appointmentService.readById(appId).orElseThrow(EntityNotFoundException::new);
        if (appointment.equals(patientCase.getAppointment()) && patientCase.equals(appointment.getPatientCase())) {
            appointment.setPatientCase(null);
        }
    }
}
