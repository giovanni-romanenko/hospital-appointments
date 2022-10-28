package cz.cvut.fit.tjv.hospital_appointments.api.converter;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;

import java.util.Collection;
import java.util.stream.Collectors;

public class DoctorConverter {

    public static Doctor fromDto(DoctorDto dto) {
        return new Doctor(dto.getId(), dto.getName(), dto.getPosition());
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
                .collect(Collectors.toList());
    }
}
