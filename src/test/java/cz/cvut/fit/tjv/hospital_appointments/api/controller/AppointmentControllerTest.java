package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import cz.cvut.fit.tjv.hospital_appointments.business.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private AppointmentService appointmentService;

    @BeforeEach
    public void prepare() {
        // todo - fill in the data
    }
}
