package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.AppointmentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AppointmentServiceTest {

    @Autowired private AppointmentService appointmentService;
    @MockBean private AppointmentJpaRepository repository;

    @BeforeEach
    public void prepare() {
        // todo - fill in data
    }
}
