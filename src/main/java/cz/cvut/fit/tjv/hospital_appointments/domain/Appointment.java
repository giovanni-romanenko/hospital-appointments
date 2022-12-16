package cz.cvut.fit.tjv.hospital_appointments.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderWithoutRelations")
@Entity
public class Appointment implements Serializable {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ToString.Include
    @Column(nullable = false)
    private LocalDateTime fromTime;
    @ToString.Include
    @Column(nullable = false)
    private LocalDateTime toTime;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "patient_case_fk")
    @Builder.Default
    private PatientCase patientCase = null;
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "doctor_fk")
    @Builder.Default
    private Doctor doctor = null;
}
