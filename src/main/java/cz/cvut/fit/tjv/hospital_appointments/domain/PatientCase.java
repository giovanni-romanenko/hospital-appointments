package cz.cvut.fit.tjv.hospital_appointments.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
public class PatientCase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String patientName;
    @Column
    private String problem;

    @OneToOne(mappedBy = "patientCase")
    private Appointment appointment;
    @ManyToMany
    @JoinTable(
            name = "qualification_to_treat_patient_case",
            joinColumns = @JoinColumn(name = "patient_case_fk"),
            inverseJoinColumns = @JoinColumn(name = "doctor_fk")
    )
    private Set<Doctor> qualifiedDoctors = new HashSet<>();

    public PatientCase(Long id, String patientName, String problem) {
        this.id = id;
        this.patientName = patientName;
        this.problem = problem;
    }
}
