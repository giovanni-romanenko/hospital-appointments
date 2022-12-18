package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.views.PatientCaseViews;
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
public class PatientCaseDto {

    @Schema(requiredMode = REQUIRED)
    @JsonView(PatientCaseViews.FullDataWithId.class)
    private Long id;
    @Schema(requiredMode = NOT_REQUIRED, example = "Bob")
    @JsonView(PatientCaseViews.FullDataWithoutId.class)
    private String patientName;
    @Schema(requiredMode = NOT_REQUIRED, example = "Headache")
    @JsonView(PatientCaseViews.FullDataWithoutId.class)
    private String problem;
}
