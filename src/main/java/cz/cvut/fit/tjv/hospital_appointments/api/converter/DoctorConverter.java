package cz.cvut.fit.tjv.hospital_appointments.api.converter;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;

import java.util.Collection;

public class DoctorConverter {

    public static Doctor fromDto(DoctorDto dto) {
        return Doctor.builderWithoutRelations()
                .id(dto.getId())
                .name(dto.getName())
                .position(dto.getPosition())
                .build();
    }

    public static DoctorDto toDto(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .position(doctor.getPosition())
                .build();
    }

    public static Collection<DoctorDto> toDtoMany(Collection<Doctor> doctors) {
        return doctors.stream()
                .map(DoctorConverter::toDto)
                .toList();
    }
}
