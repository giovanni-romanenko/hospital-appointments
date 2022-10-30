package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.views.DoctorViews;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {

    @JsonView(DoctorViews.FullDataWithId.class)
    private Long id;
    @JsonView(DoctorViews.FullDataWithoutId.class)
    private String name;
    @JsonView(DoctorViews.FullDataWithoutId.class)
    private String position;
}
