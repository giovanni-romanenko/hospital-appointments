package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.views.PatientCaseViews;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientCaseDto {

    @JsonView(PatientCaseViews.FullDataWithId.class)
    private Long id;
    @JsonView(PatientCaseViews.FullDataWithoutId.class)
    private String patientName;
    @JsonView(PatientCaseViews.FullDataWithoutId.class)
    private String problem;
}
