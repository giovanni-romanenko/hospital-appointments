package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.AppointmentConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.PatientCaseConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.api.views.AppointmentViews;
import cz.cvut.fit.tjv.hospital_appointments.api.views.DoctorViews;
import cz.cvut.fit.tjv.hospital_appointments.api.views.PatientCaseViews;
import cz.cvut.fit.tjv.hospital_appointments.business.DoctorService;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.exception.CreatedEntityNullIdException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.DoctorConverter.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/doctors/{id}")
    public DoctorDto readById(@PathVariable Long id) {
        return toDto(doctorService.readById(id).orElseThrow(EntityNotFoundException::new));
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/doctors")
    public Collection<DoctorDto> readAll() {
        return toDtoMany(doctorService.readAll());
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @PostMapping("/doctors")
    public DoctorDto create(
            @JsonView(DoctorViews.FullDataWithoutId.class)
            @RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorService.create(fromDto(doctorDto));
        if (doctor.getId() == null) {
            throw new CreatedEntityNullIdException(doctor);
        }
        return toDto(doctor);
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @PutMapping("/doctors/{id}")
    public DoctorDto update(
            @PathVariable Long id,
            @JsonView(DoctorViews.FullDataWithoutId.class)
            @RequestBody DoctorDto doctorDto) {
        Doctor doctor = fromDto(doctorDto);
        doctor.setId(id);
        return toDto(doctorService.update(doctor));
    }

    @DeleteMapping("/doctors/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        doctorService.deleteById(id);
    }

    @JsonView(AppointmentViews.FullDataWithId.class)
    @GetMapping("/doctors/{id}/appointments")
    public Collection<AppointmentDto> readAllAppointmentsOfDoctor(@PathVariable Long id) {
        return AppointmentConverter.toDtoMany(doctorService.readAllAppointmentsOfDoctor(id));
    }

    @PutMapping("/doctors/{docId}/appointments/{appId}") // todo - refactor this put into appointment controller
    @ResponseStatus(NO_CONTENT)
    public void updateAppointmentForDoctor(@PathVariable Long docId, @PathVariable Long appId) {
        doctorService.updateAppointmentForDoctor(docId, appId);
    }

    @JsonView(PatientCaseViews.FullDataWithId.class)
    @GetMapping("/doctors/{id}/patient_cases")
    public Collection<PatientCaseDto> readAllPatientCasesTreatableByDoctor(@PathVariable Long id) {
        return PatientCaseConverter.toDtoMany(doctorService.readAllPatientCasesTreatableByDoctor(id));
    }

    @PutMapping("/doctors/{docId}/patient_cases/{caseId}")
    @ResponseStatus(NO_CONTENT)
    public void updateDoctorCanTreatPatientCase(@PathVariable Long docId, @PathVariable Long caseId) {
        doctorService.updateDoctorCanTreatPatientCase(docId, caseId);
    }

    @DeleteMapping("/doctors/{docId}/patient_cases/{caseId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteDoctorCanTreatPatientCase(@PathVariable Long docId, @PathVariable Long caseId) {
        doctorService.deleteDoctorCanTreatPatientCase(docId, caseId);
    }
}
