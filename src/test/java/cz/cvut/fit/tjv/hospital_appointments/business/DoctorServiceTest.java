package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.DoctorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class DoctorServiceTest {

    @Autowired private DoctorService doctorService;
    @MockBean private DoctorJpaRepository repository;
    @MockBean private AppointmentService appointmentService;
    @MockBean private PatientCaseService patientCaseService;

    @BeforeEach
    public void prepare() {
        // todo - fill in data
    }
}
