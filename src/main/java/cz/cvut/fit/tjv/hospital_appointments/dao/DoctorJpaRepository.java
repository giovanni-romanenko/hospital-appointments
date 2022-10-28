package cz.cvut.fit.tjv.hospital_appointments.dao;

import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorJpaRepository extends JpaRepository<Doctor, Long> {
}
