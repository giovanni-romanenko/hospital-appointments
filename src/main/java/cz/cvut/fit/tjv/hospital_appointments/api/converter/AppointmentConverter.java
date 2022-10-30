package cz.cvut.fit.tjv.hospital_appointments.api.converter;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;

import java.util.Collection;
import java.util.stream.Collectors;

public class AppointmentConverter {

    public static Appointment fromDto(AppointmentDto dto) {
        return new Appointment(dto.getId(), dto.getFromTime(), dto.getToTime());
    }

    public static AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .fromTime(appointment.getFromTime())
                .toTime(appointment.getToTime())
                .build();
    }

    public static Collection<AppointmentDto> toDtoMany(Collection<Appointment> appointments) {
        return appointments.stream()
                .map(AppointmentConverter::toDto)
                .collect(Collectors.toList());
    }
}
