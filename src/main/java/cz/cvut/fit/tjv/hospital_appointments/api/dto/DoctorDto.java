package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.views.DoctorViews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {

    @Schema(requiredMode = REQUIRED)
    @JsonView(DoctorViews.FullDataWithId.class)
    private Long id;
    @Schema(requiredMode = NOT_REQUIRED, example = "John")
    @JsonView(DoctorViews.FullDataWithoutId.class)
    private String name;
    @Schema(requiredMode = NOT_REQUIRED, example = "Medic")
    @JsonView(DoctorViews.FullDataWithoutId.class)
    private String position;
}
