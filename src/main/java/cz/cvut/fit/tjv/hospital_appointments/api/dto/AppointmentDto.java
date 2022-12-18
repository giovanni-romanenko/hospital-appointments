package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.views.AppointmentViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    @Schema(requiredMode = REQUIRED)
    @JsonView(AppointmentViews.FullDataWithId.class)
    private Long id;
    @Schema(requiredMode = REQUIRED, type = "string", example = "20/10/2001 12:30:30")
    @JsonView(AppointmentViews.FullDataWithoutId.class)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm[:ss]")
    private LocalDateTime fromTime;
    @Schema(requiredMode = REQUIRED, type = "string", example = "20/10/2001 13:30:30")
    @JsonView(AppointmentViews.FullDataWithoutId.class)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm[:ss]")
    private LocalDateTime toTime;
}
