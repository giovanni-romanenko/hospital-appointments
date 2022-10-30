package cz.cvut.fit.tjv.hospital_appointments.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime fromTime;
    @Column(nullable = false)
    private LocalDateTime toTime;

    @OneToOne
    @JoinColumn(name = "patient_case_fk")
    private PatientCase patientCase;
    @ManyToOne
    @JoinColumn(name = "doctor_fk")
    private Doctor doctor;

    public Appointment(Long id, LocalDateTime fromTime, LocalDateTime toTime) {
        this.id = id;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }
}
