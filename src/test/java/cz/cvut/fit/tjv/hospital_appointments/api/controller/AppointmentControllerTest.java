package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.business.AppointmentService;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper jsonMapper;
    @MockBean private AppointmentService appointmentService;

    private final LocalDateTime time1 = LocalDateTime.of(2000, 3, 4, 5, 6, 7);
    private final LocalDateTime time2 = LocalDateTime.of(2001, 4, 5, 6, 7, 8);
    private final LocalDateTime time3 = LocalDateTime.of(2002, 10, 10, 10, 10, 10);
    private final LocalDateTime time4 = LocalDateTime.of(2003, 11, 11, 11, 11, 11);
    private final LocalDateTime time5 = LocalDateTime.of(2004, 5, 4, 6, 3, 2);
    private final LocalDateTime time6 = LocalDateTime.of(2005, 1, 1, 0, 9, 1);

    private final Appointment app1 = new Appointment(1L, time1, time2);
    private final Appointment app2 = new Appointment(2L, time3, time4);
    private final Appointment app3 = new Appointment(3L, time5, time6);
    private final Appointment updatedApp3 = new Appointment(3L, time1, time6);
    private final Appointment createdApp4 = new Appointment(null, time4, time5);
    private final Appointment createdApp4WithId = new Appointment(4L, time4, time5);

    @BeforeEach
    public void prepare() {
        Mockito.when(appointmentService.readById(1L)).thenReturn(Optional.of(app1));
        Mockito.when(appointmentService.readById(2L)).thenReturn(Optional.of(app2));
        Mockito.when(appointmentService.readById(3L)).thenReturn(Optional.of(app3));
        Mockito.when(appointmentService.readById(4L)).thenReturn(Optional.empty());
        Mockito.when(appointmentService.readAll()).thenReturn(List.of(app1, app2, app3));
        Mockito.when(appointmentService.create(createdApp4)).thenReturn(createdApp4WithId);
        Mockito.when(appointmentService.update(updatedApp3)).thenReturn(updatedApp3);
    }

    @Test
    public void checkAppointmentSuccessfulCreateReturnsCorrectData() throws Exception {
        mockMvc.perform(post("/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(AppointmentDto.builder().fromTime(time4).toTime(time5).build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(
                        AppointmentDto.builder().id(4L).fromTime(time4).toTime(time5).build())));
    }

    @Test
    public void checkAppointmentCreateErrorMessageWhenFromTimeParameterIsNull() {
        // todo - simple post
    }

    @Test
    public void checkAppointmentSuccessfulReadReturnsCorrectData() {
        // todo - simple get
    }

    @Test
    public void checkAppointmentReadErrorMessageWhenIdDoesNotExist() {
        // todo - simple get
    }

    @Test
    public void checkAllAppointmentsReadReturnsCorrectData() {
        // todo - get all
    }

    @Test
    public void checkAppointmentSuccessfulUpdateReturnsCorrectData() {
        // todo - update
    }

    @Test
    public void checkAppointmentUpdateErrorMessageWhenIdDoesNotExist() {
        // todo - update
    }

    @Test
    public void checkAppointmentUpdateErrorMessageWheToTimeParameterIsNull() {
        // todo - update
    }

    @Test
    public void checkAppointmentSuccessfulDeleteResponseCode() {
        // todo - delete, check that service method was called as well
    }
}
