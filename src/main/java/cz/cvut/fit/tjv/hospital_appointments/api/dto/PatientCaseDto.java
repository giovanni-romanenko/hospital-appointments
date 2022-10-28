package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientCaseDto {

    // todo - annotation maybe
    private Long id;
    private String patientName;
    private String problem;
}
