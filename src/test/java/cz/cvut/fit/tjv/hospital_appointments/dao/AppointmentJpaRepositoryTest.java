package cz.cvut.fit.tjv.hospital_appointments.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AppointmentJpaRepositoryTest {
    // todo - use @Autowired

    @Test
    public void checkFindAllAppointmentsBreakingIntegrityConstraint() {
        // todo - create DB elements and check query results
    }
}
