package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.business.PatientCaseService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class PatientCaseController {

    private final PatientCaseService patientCaseService;

    public PatientCaseController(PatientCaseService patientCaseService) {
        this.patientCaseService = patientCaseService;
    }

    @GetMapping("/patient_cases/{id}")
    public PatientCaseDto readById(@PathVariable Long id) {
        return // todo
    }

    @GetMapping("/patient_cases")
    public Collection<PatientCaseDto> readAll() {
        return // todo
    }

    @PostMapping("/patient_cases")
    public PatientCaseDto create() { // todo
        // todo
    }

    @PutMapping("/patient_cases/{id}")
    public PatientCaseDto update(@PathVariable Long id, ) { // todo
        // todo
    }

    @DeleteMapping("/patient_cases/{id}")
    public void deleteById(@PathVariable Long id) {
        patientCaseService.deleteById(id);
    }
}
