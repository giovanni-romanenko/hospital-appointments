package cz.cvut.fit.tjv.hospital_appointments.api.converter;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;

import java.util.Collection;

public class AppointmentConverter {

    public static Appointment fromDto(AppointmentDto dto) {
        return Appointment.builderWithoutRelations()
                .id(dto.getId())
                .fromTime(dto.getFromTime())
                .toTime(dto.getToTime())
                .build();
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
                .toList();
    }
}
