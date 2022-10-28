package cz.cvut.fit.tjv.hospital_appointments.api.converter;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;

import java.util.Collection;
import java.util.stream.Collectors;

public class AppointmentConverter {

    public static Appointment fromDto(AppointmentDto dto) {
        return new Appointment(dto.getId(), dto.getFrom(), dto.getTo());
    }

    public static AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .from(appointment.getFrom())
                .to(appointment.getTo())
                .build();
    }

    public static Collection<AppointmentDto> toDtoMany(Collection<Appointment> appointments) {
        return appointments.stream()
                .map(AppointmentConverter::toDto)
                .collect(Collectors.toList());
    }
}
