package cz.cvut.fit.tjv.hospital_appointments.dao;

import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<Appointment, Long> {
    // todo - write JPQL for checking IC3: cycle is fine in the model
    // findAllAppointmentsBreakingIntegrityConstraint
}
