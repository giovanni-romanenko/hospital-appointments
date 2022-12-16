package cz.cvut.fit.tjv.hospital_appointments.api.converter;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;

import java.util.Collection;

public class PatientCaseConverter {

    public static PatientCase fromDto(PatientCaseDto dto) {
        return PatientCase.builderWithoutRelations()
                .id(dto.getId())
                .patientName(dto.getPatientName())
                .problem(dto.getProblem())
                .build();
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
                .toList();
    }
}
