package cz.cvut.fit.tjv.hospital_appointments.dao;

import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientCaseJpaRepository extends JpaRepository<PatientCase, Long> {
}
