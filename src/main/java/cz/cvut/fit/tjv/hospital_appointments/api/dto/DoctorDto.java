package cz.cvut.fit.tjv.hospital_appointments.api.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {

    // todo - annotation maybe
    private Long id;
    private String name;
    private String position;
}
