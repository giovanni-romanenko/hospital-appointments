package cz.cvut.fit.tjv.hospital_appointments.dao;

import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorJpaRepository extends JpaRepository<Doctor, Long> {

    @Query("""
           SELECT DISTINCT doc
           FROM Appointment app
           INNER JOIN Doctor doc ON doc.id = app.doctor.id
           INNER JOIN PatientCase pat ON pat.id = app.patientCase.id
           WHERE pat MEMBER OF doc.treatablePatientCases
           """)
    List<Doctor> findAllDoctorsWhoHaveAppointmentsWithPatientCasesThatTheyCanNotWorkOn();
}
