package cz.cvut.fit.tjv.hospital_appointments.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime from;
    @Column(nullable = false)
    private LocalDateTime to;

    @OneToOne
    @JoinColumn(name = "patient_case_fk")
    private PatientCase patientCase;
    @ManyToOne
    @JoinColumn(name = "doctor_fk")
    private Doctor doctor;

    public Appointment(Long id, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }
}
