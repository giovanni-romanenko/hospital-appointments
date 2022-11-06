package cz.cvut.fit.tjv.hospital_appointments.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
public class PatientCase implements Serializable {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ToString.Include
    @Column
    private String patientName;
    @ToString.Include
    @Column
    private String problem;

    @OneToOne(mappedBy = "patientCase", fetch = EAGER)
    private Appointment appointment;
    @ManyToMany(fetch = EAGER)
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
