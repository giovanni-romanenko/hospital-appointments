package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.DoctorJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class DoctorService extends
        AbstractCrudService<Long, Doctor, DoctorJpaRepository> {

    private final AppointmentService appointmentService;
    private final PatientCaseService patientCaseService;

    public DoctorService(DoctorJpaRepository repository, AppointmentService appointmentService,
                         PatientCaseService patientCaseService) {
        super(repository);
        this.appointmentService = appointmentService;
        this.patientCaseService = patientCaseService;
    }

    @Override
    public Doctor update(Doctor doctor) {
        if (repository.existsById(doctor.getId())) {
            return repository.save(doctor);
        }
        throw new EntityNotFoundException(doctor);
    }

    @Transactional
    public void updateAppointmentForDoctor(@NonNull Long docId, @NonNull Long appId) {
        Doctor doctor = readById(docId).orElseThrow(EntityNotFoundException::new);
        Appointment appointment = appointmentService.readById(appId).orElseThrow(EntityNotFoundException::new);
        appointment.setDoctor(doctor);
    }

    @Transactional
    public Collection<PatientCase> readAllPatientCasesTreatableByDoctor(@NonNull Long docId) {
        Doctor doctor = readById(docId).orElseThrow(EntityNotFoundException::new);
        return doctor.getTreatablePatientCases();
    }

    @Transactional
    public Collection<Appointment> readAllAppointmentsOfDoctor(@NonNull Long docId) {
        Doctor doctor = readById(docId).orElseThrow(EntityNotFoundException::new);
        return doctor.getAppointments();
    }

    @Transactional
    public void updateDoctorCanTreatPatientCase(@NonNull Long docId, @NonNull Long caseId) {
        Doctor doctor = readById(docId).orElseThrow(EntityNotFoundException::new);
        PatientCase patientCase = patientCaseService.readById(caseId).orElseThrow(EntityNotFoundException::new);
        doctor.getTreatablePatientCases().add(patientCase); // todo - check if it works correctly
    }

    @Transactional
    public void deleteDoctorCanTreatPatientCase(@NonNull Long docId, @NonNull Long caseId) {
        Doctor doctor = readById(docId).orElseThrow(EntityNotFoundException::new);
        PatientCase patientCase = patientCaseService.readById(caseId).orElseThrow(EntityNotFoundException::new);
        doctor.getTreatablePatientCases().remove(patientCase);
    }
}
