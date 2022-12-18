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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.AppointmentConverter.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "Read appointment")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AppointmentDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(AppointmentViews.FullDataWithId.class)
    @GetMapping("/appointments/{id}")
    public AppointmentDto readById(@PathVariable Long id) {
        return toDto(appointmentService.readById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Operation(summary = "Read all appointments")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class))))
    @JsonView(AppointmentViews.FullDataWithId.class)
    @GetMapping("/appointments")
    public Collection<AppointmentDto> readAll() {
        return toDtoMany(appointmentService.readAll());
    }

    @Operation(summary = "Create appointment")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AppointmentDto.class)))
    @ApiResponse(responseCode = "422", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
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

    @Operation(summary = "Update appointment")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AppointmentDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "409", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "422", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
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

    @Operation(summary = "Delete appointment")
    @DeleteMapping("/appointments/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        appointmentService.deleteById(id);
    }

    @Operation(summary = "Read doctor of appointment")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = DoctorDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/appointments/{id}/doctors")
    public DoctorDto readDoctorOfAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.readById(id).orElseThrow(EntityNotFoundException::new);
        if (appointment.getDoctor() == null) {
            return null;
        }
        return DoctorConverter.toDto(appointment.getDoctor());
    }

    @Operation(summary = "Update doctor of appointment")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "409", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @PutMapping("/appointments/{appId}/doctors/{docId}")
    @ResponseStatus(NO_CONTENT)
    public void updateDoctorOfAppointment(@PathVariable Long appId, @PathVariable Long docId) {
        appointmentService.updateDoctorOfAppointment(appId, docId);
    }

    @Operation(summary = "Delete doctor of appointment")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @DeleteMapping("/appointments/{id}/doctors")
    @ResponseStatus(NO_CONTENT)
    public void deleteDoctorOfAppointment(@PathVariable Long id) {
        appointmentService.deleteDoctorOfAppointment(id);
    }

    @Operation(summary = "Read patient case of appointment")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = PatientCaseDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
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
