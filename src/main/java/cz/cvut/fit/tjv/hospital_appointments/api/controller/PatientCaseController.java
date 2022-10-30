package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.api.views.PatientCaseViews;
import cz.cvut.fit.tjv.hospital_appointments.business.PatientCaseService;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import cz.cvut.fit.tjv.hospital_appointments.exception.CreatedEntityNullIdException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.PatientCaseConverter.*;

@RestController
public class PatientCaseController {

    private final PatientCaseService patientCaseService;

    public PatientCaseController(PatientCaseService patientCaseService) {
        this.patientCaseService = patientCaseService;
    }

    @JsonView(PatientCaseViews.FullDataWithId.class)
    @GetMapping("/patient_cases/{id}")
    public PatientCaseDto readById(@PathVariable Long id) {
        return toDto(patientCaseService.readById(id).orElseThrow(EntityNotFoundException::new));
    }

    @JsonView(PatientCaseViews.FullDataWithId.class)
    @GetMapping("/patient_cases")
    public Collection<PatientCaseDto> readAll() {
        return toDtoMany(patientCaseService.readAll());
    }

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

    @DeleteMapping("/patient_cases/{id}")
    public void deleteById(@PathVariable Long id) {
        patientCaseService.deleteById(id);
    }
}
