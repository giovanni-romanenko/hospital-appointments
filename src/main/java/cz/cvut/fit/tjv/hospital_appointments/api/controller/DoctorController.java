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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.DoctorConverter.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "Read doctor")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = DoctorDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/doctors/{id}")
    public DoctorDto readById(@PathVariable Long id) {
        return toDto(doctorService.readById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Operation(summary = "Read all doctors")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class))))
    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/doctors")
    public Collection<DoctorDto> readAll() {
        return toDtoMany(doctorService.readAll());
    }

    @Operation(summary = "Create doctor")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = DoctorDto.class)))
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

    @Operation(summary = "Update doctor")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = DoctorDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
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

    @Operation(summary = "Delete doctor")
    @DeleteMapping("/doctors/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        doctorService.deleteById(id);
    }

    @Operation(summary = "Read all appointments of doctor")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class))))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(AppointmentViews.FullDataWithId.class)
    @GetMapping("/doctors/{id}/appointments")
    public Collection<AppointmentDto> readAllAppointmentsOfDoctor(@PathVariable Long id) {
        return AppointmentConverter.toDtoMany(doctorService.readAllAppointmentsOfDoctor(id));
    }

    @Operation(summary = "Read all patient cases treatable by doctor")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = PatientCaseDto.class))))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(PatientCaseViews.FullDataWithId.class)
    @GetMapping("/doctors/{id}/patient_cases")
    public Collection<PatientCaseDto> readAllPatientCasesTreatableByDoctor(@PathVariable Long id) {
        return PatientCaseConverter.toDtoMany(doctorService.readAllPatientCasesTreatableByDoctor(id));
    }
}
