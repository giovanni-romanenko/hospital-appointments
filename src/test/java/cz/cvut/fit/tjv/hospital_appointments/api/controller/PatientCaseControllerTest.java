package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import cz.cvut.fit.tjv.hospital_appointments.business.PatientCaseService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PatientCaseController.class)
public class PatientCaseControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private PatientCaseService patientCaseService;

    @BeforeEach
    public void prepare() {
        // todo - fill in the data
    }
}
