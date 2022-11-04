package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.PatientCaseJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PatientCaseServiceTest {

    @Autowired private PatientCaseService patientCaseService;
    @MockBean private PatientCaseJpaRepository repository;
    @MockBean private AppointmentService appointmentService;

    @BeforeEach
    public void prepare() {
        // todo - fill in data
    }
}
