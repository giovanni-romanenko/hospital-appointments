package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.views.AppointmentViews;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    @JsonView(AppointmentViews.FullDataWithId.class)
    private Long id;
    @JsonView(AppointmentViews.FullDataWithoutId.class)
    private LocalDateTime from;
    @JsonView(AppointmentViews.FullDataWithoutId.class)
    private LocalDateTime to;
}
