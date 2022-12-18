package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.AppointmentConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.DoctorConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.api.views.AppointmentViews;
import cz.cvut.fit.tjv.hospital_appointments.api.views.DoctorViews;
import cz.cvut.fit.tjv.hospital_appointments.api.views.PatientCaseViews;
import cz.cvut.fit.tjv.hospital_appointments.business.PatientCaseService;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import cz.cvut.fit.tjv.hospital_appointments.exception.CreatedEntityNullIdException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.PatientCaseConverter.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
public class PatientCaseController {

    private final PatientCaseService patientCaseService;

    public PatientCaseController(PatientCaseService patientCaseService) {
        this.patientCaseService = patientCaseService;
    }

    @Operation(summary = "Read patient case")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = PatientCaseDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(PatientCaseViews.FullDataWithId.class)
    @GetMapping("/patient_cases/{id}")
    public PatientCaseDto readById(@PathVariable Long id) {
        return toDto(patientCaseService.readById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Operation(summary = "Read all patient cases")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = PatientCaseDto.class))))
    @JsonView(PatientCaseViews.FullDataWithId.class)
    @GetMapping("/patient_cases")
    public Collection<PatientCaseDto> readAll() {
        return toDtoMany(patientCaseService.readAll());
    }

    @Operation(summary = "Create patient case")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = PatientCaseDto.class)))
    @JsonView(PatientCaseViews.FullDataWithId.class)
    @PostMapping("/patient_cases")
    public PatientCaseDto create(
            @JsonView(PatientCaseViews.FullDataWithoutId.class)
            @RequestBody PatientCaseDto patientCaseDto) {
        PatientCase patientCase = patientCaseService.create(fromDto(patientCaseDto));
        if (patientCase.getId() == null) {
            throw new CreatedEntityNullIdException(patientCase);
        }
        return toDto(patientCase);
    }

    @Operation(summary = "Update patient case")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = PatientCaseDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(PatientCaseViews.FullDataWithId.class)
    @PutMapping("/patient_cases/{id}")
    public PatientCaseDto update(
            @PathVariable Long id,
            @JsonView(PatientCaseViews.FullDataWithoutId.class)
            @RequestBody PatientCaseDto patientCaseDto) {
        PatientCase patientCase = fromDto(patientCaseDto);
        patientCase.setId(id);
        return toDto(patientCaseService.update(patientCase));
    }

    @Operation(summary = "Delete patient case")
    @DeleteMapping("/patient_cases/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        patientCaseService.deleteById(id);
    }

    @Operation(summary = "Read all doctors who can work on patient case")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class))))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/patient_cases/{id}/doctors")
    public Collection<DoctorDto> readAllDoctorsWhoCanWorkOnPatientCase(@PathVariable Long id) {
        return DoctorConverter.toDtoMany(patientCaseService.readAllDoctorsWhoCanWorkOnPatientCase(id));
    }

    @Operation(summary = "Read appointment of patient case")
    @ApiResponse(responseCode = "200", content = @Content(
            mediaType = APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AppointmentDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @JsonView(AppointmentViews.FullDataWithId.class)
    @GetMapping("/patient_cases/{id}/appointments")
    public AppointmentDto readAppointmentOfPatientCase(@PathVariable Long id) {
        PatientCase patientCase = patientCaseService.readById(id).orElseThrow(EntityNotFoundException::new);
        if (patientCase.getAppointment() == null) {
            return null;
        }
        return AppointmentConverter.toDto(patientCase.getAppointment());
    }

    @Operation(summary = "Update appointment of patient case")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "409", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @PutMapping("/patient_cases/{caseId}/appointments/{appId}")
    @ResponseStatus(NO_CONTENT)
    public void updateAppointmentOfPatientCase(@PathVariable Long caseId, @PathVariable Long appId) {
        patientCaseService.updateAppointmentOfPatientCase(caseId, appId);
    }

    @Operation(summary = "Delete appointment of patient case")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @DeleteMapping("/patient_cases/{caseId}/appointments/{appId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteAppointmentOfPatientCase(@PathVariable Long caseId, @PathVariable Long appId) {
        patientCaseService.deleteAppointmentOfPatientCase(caseId, appId);
    }

    @Operation(summary = "Update patient case can be treated by doctor")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @PutMapping("/patient_cases/{caseId}/doctors/{docId}")
    @ResponseStatus(NO_CONTENT)
    public void updatePatientCaseCanBeTreatedByDoctor(@PathVariable Long caseId, @PathVariable Long docId) {
        patientCaseService.updatePatientCaseCanBeTreatedByDoctor(caseId, docId);
    }

    @Operation(summary = "Delete patient case can be treated by doctor")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @ApiResponse(responseCode = "409", content = @Content(
            mediaType = TEXT_PLAIN_VALUE))
    @DeleteMapping("/patient_cases/{caseId}/doctors/{docId}")
    @ResponseStatus(NO_CONTENT)
    public void deletePatientCaseCanBeTreatedByDoctor(@PathVariable Long caseId, @PathVariable Long docId) {
        patientCaseService.deletePatientCaseCanBeTreatedByDoctor(caseId, docId);
    }
}
