package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    // todo - annotation maybe
    private Long id;
    private LocalDateTime from;
    private LocalDateTime to;
}
