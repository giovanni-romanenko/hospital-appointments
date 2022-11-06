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
public class Doctor implements Serializable {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ToString.Include
    @Column
    private String name;
    @ToString.Include
    @Column
    private String position;

    @OneToMany(mappedBy = "doctor", fetch = EAGER)
    private Set<Appointment> appointments = new HashSet<>();
    @ManyToMany(mappedBy = "qualifiedDoctors", fetch = EAGER)
    private Set<PatientCase> treatablePatientCases = new HashSet<>();

    public Doctor(Long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }
}
