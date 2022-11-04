package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import cz.cvut.fit.tjv.hospital_appointments.business.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private DoctorService doctorService;

    @BeforeEach
    public void prepare() {
        // todo - fill in the data
    }
}
