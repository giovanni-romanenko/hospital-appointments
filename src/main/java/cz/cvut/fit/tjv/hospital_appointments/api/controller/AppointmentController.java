package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.DoctorConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.PatientCaseConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.api.views.AppointmentViews;
import cz.cvut.fit.tjv.hospital_appointments.api.views.DoctorViews;
import cz.cvut.fit.tjv.hospital_appointments.api.views.PatientCaseViews;
import cz.cvut.fit.tjv.hospital_appointments.business.AppointmentService;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.exception.CreatedEntityNullIdException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.AppointmentConverter.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @JsonView(AppointmentViews.FullDataWithId.class)
    @GetMapping("/appointments/{id}")
    public AppointmentDto readById(@PathVariable Long id) {
        return toDto(appointmentService.readById(id).orElseThrow(EntityNotFoundException::new));
    }

    @JsonView(AppointmentViews.FullDataWithId.class)
    @GetMapping("/appointments")
    public Collection<AppointmentDto> readAll() {
        return toDtoMany(appointmentService.readAll());
    }

    @JsonView(AppointmentViews.FullDataWithId.class)
    @PostMapping("/appointments")
    public AppointmentDto create(
            @JsonView(AppointmentViews.FullDataWithoutId.class)
            @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = appointmentService.create(fromDto(appointmentDto));
        if (appointment.getId() == null) {
            throw new CreatedEntityNullIdException(appointment);
        }
        return toDto(appointment);
    }

    @JsonView(AppointmentViews.FullDataWithId.class)
    @PutMapping("/appointments/{id}")
    public AppointmentDto update(
            @PathVariable Long id,
            @JsonView(AppointmentViews.FullDataWithoutId.class)
            @RequestBody AppointmentDto appointmentDto) {
        Appointment appointment = fromDto(appointmentDto);
        appointment.setId(id);
        return toDto(appointmentService.update(appointment));
    }

    @DeleteMapping("/appointments/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        appointmentService.deleteById(id);
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/appointments/{id}/doctors")
    public DoctorDto readDoctorOfAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.readById(id).orElseThrow(EntityNotFoundException::new);
        if (appointment.getDoctor() == null) {
            return null;
        }
        return DoctorConverter.toDto(appointment.getDoctor());
    }

    @DeleteMapping("/appointments/{id}/doctors")
    @ResponseStatus(NO_CONTENT)
    public void deleteDoctorOfAppointment(@PathVariable Long id) {
        appointmentService.deleteDoctorOfAppointment(id);
    }

    @JsonView(PatientCaseViews.FullDataWithId.class)
    @GetMapping("/appointments/{id}/patient_cases")
    public PatientCaseDto readPatientCaseOfAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.readById(id).orElseThrow(EntityNotFoundException::new);
        if (appointment.getPatientCase() == null) {
            return null;
        }
        return PatientCaseConverter.toDto(appointment.getPatientCase());
    }
}
