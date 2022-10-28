package cz.cvut.fit.tjv.hospital_appointments.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Doctor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String position;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments = new HashSet<>();
    @ManyToMany(mappedBy = "qualifiedDoctors")
    private Set<PatientCase> treatablePatientCases = new HashSet<>();

    public Doctor(Long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }
}
