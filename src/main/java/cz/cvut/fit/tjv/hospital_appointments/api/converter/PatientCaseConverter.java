package cz.cvut.fit.tjv.hospital_appointments.api.converter;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;

import java.util.Collection;
import java.util.stream.Collectors;

public class PatientCaseConverter {

    public static PatientCase fromDto(PatientCaseDto dto) {
        return new PatientCase(dto.getId(), dto.getPatientName(), dto.getProblem());
    }

    public static PatientCaseDto toDto(PatientCase patientCase) {
        return PatientCaseDto.builder()
                .id(patientCase.getId())
                .patientName(patientCase.getPatientName())
                .problem(patientCase.getProblem())
                .build();
    }

    public static Collection<PatientCaseDto> toDtoMany(Collection<PatientCase> patientCases) {
        return patientCases.stream()
                .map(PatientCaseConverter::toDto)
                .collect(Collectors.toList());
    }
}
