package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.AppointmentConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.business.AppointmentService;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.exception.DeletingNonExistingEntityException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EndTimeBeforeStartTimeException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import cz.cvut.fit.tjv.hospital_appointments.exception.NonNullableFieldIsNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.AppointmentConverter.toDto;
import static org.mockito.Mockito.*;
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

    private final Appointment app1 = Appointment.builderWithoutRelations().id(1L).fromTime(time1).toTime(time2).build();
    private final Appointment app2 = Appointment.builderWithoutRelations().id(2L).fromTime(time3).toTime(time4).build();
    private final Appointment app3 = Appointment.builderWithoutRelations().id(3L).fromTime(time5).toTime(time6).build();
    private final List<Appointment> allApps = List.of(app1, app2, app3);

    @BeforeEach
    public void prepare() {
        when(appointmentService.readById(1L)).thenReturn(Optional.of(app1));
        when(appointmentService.readById(2L)).thenReturn(Optional.of(app2));
        when(appointmentService.readById(3L)).thenReturn(Optional.of(app3));
        when(appointmentService.readAll()).thenReturn(allApps);
    }

    @Test
    public void checkAppointmentSuccessfulCreateReturnsCorrectData() throws Exception {
        Appointment createdApp = Appointment.builderWithoutRelations().id(null).fromTime(time4).toTime(time5).build();
        Appointment createdAppWithId = Appointment.builderWithoutRelations().id(4L).fromTime(time4).toTime(time5).build();
        when(appointmentService.create(createdApp)).thenReturn(createdAppWithId);

        mockMvc.perform(post("/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(createdApp))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(createdAppWithId))));
    }

    @Test
    public void checkAppointmentCreateErrorMessageWhenFromTimeParameterIsNull() throws Exception {
        Appointment createdApp = Appointment.builderWithoutRelations().id(null).fromTime(null).toTime(time2).build();
        AppointmentDto createdAppDto = AppointmentDto.builder().id(7L).fromTime(createdApp.getFromTime()).toTime(createdApp.getToTime()).build();
        when(appointmentService.create(createdApp)).thenThrow(NonNullableFieldIsNullException.class);

        mockMvc.perform(post("/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(createdAppDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Non-Nullable field of entity can not be null"));
    }

    @Test
    public void checkAppointmentCreateErrorMessageWhenFromTimeIsAfterToTime() throws Exception {
        Appointment createdApp = Appointment.builderWithoutRelations().id(null).fromTime(time4).toTime(time3).build();
        when(appointmentService.create(createdApp)).thenThrow(EndTimeBeforeStartTimeException.class);

        mockMvc.perform(post("/appointments")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(createdApp))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("End time must be after start time"));
    }

    @Test
    public void checkAppointmentSuccessfulReadReturnsCorrectData() throws Exception {
        mockMvc.perform(get("/appointments/2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(app2))));
    }

    @Test
    public void checkAppointmentReadErrorMessageWhenIdDoesNotExist() throws Exception {
        when(appointmentService.readById(1001L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/appointments/1001")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    public void checkAllAppointmentsReadReturnsCorrectData() throws Exception {
        List<AppointmentDto> allAppDto = allApps.stream().map(AppointmentConverter::toDto).toList();

        mockMvc.perform(get("/appointments")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(allAppDto)));
    }

    @Test
    public void checkAppointmentSuccessfulUpdateReturnsCorrectData() throws Exception {
        Appointment updatedApp3 = Appointment.builderWithoutRelations().id(3L).fromTime(time1).toTime(time6).build();
        when(appointmentService.update(updatedApp3)).thenReturn(updatedApp3);

        mockMvc.perform(put("/appointments/3")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedApp3))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(updatedApp3))));
    }

    @Test
    public void checkAppointmentUpdateErrorMessageWhenIdDoesNotExist() throws Exception {
        Appointment updatedApp = Appointment.builderWithoutRelations().id(1001L).fromTime(time3).toTime(time4).build();
        when(appointmentService.update(updatedApp)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/appointments/1001")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedApp))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    public void checkAppointmentUpdateErrorMessageWhenToTimeParameterIsNull() throws Exception {
        Appointment updatedApp = Appointment.builderWithoutRelations().id(2L).fromTime(time5).toTime(null).build();
        when(appointmentService.update(updatedApp)).thenThrow(NonNullableFieldIsNullException.class);

        mockMvc.perform(put("/appointments/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedApp))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Non-Nullable field of entity can not be null"));
    }

    @Test
    public void checkAppointmentUpdateErrorMessageWhenFromTimeIsAfterToTime() throws Exception {
        Appointment updatedApp = Appointment.builderWithoutRelations().id(1L).fromTime(time5).toTime(time4).build();
        when(appointmentService.update(updatedApp)).thenThrow(EndTimeBeforeStartTimeException.class);

        mockMvc.perform(put("/appointments/1")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedApp))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("End time must be after start time"));
    }

    @Test
    public void checkAppointmentDeleteResponseForExistingEntity() throws Exception {
        doNothing().when(appointmentService).deleteById(1L);

        mockMvc.perform(delete("/appointments/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void checkAppointmentDeleteResponseForNonExistingEntity() throws Exception {
        doThrow(DeletingNonExistingEntityException.class).when(appointmentService).deleteById(1001L);

        mockMvc.perform(delete("/appointments/1001")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
