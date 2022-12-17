package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.AppointmentJpaRepository;
import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import cz.cvut.fit.tjv.hospital_appointments.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AppointmentServiceTest {

    @Autowired private AppointmentService appointmentService;
    @MockBean private AppointmentJpaRepository appointmentRepository;

    private final LocalDateTime time1 = LocalDateTime.of(2000, 3, 4, 5, 6, 7);
    private final LocalDateTime time2 = LocalDateTime.of(2001, 4, 5, 6, 7, 8);
    private final LocalDateTime time3 = LocalDateTime.of(2002, 10, 10, 10, 10, 10);

    @Test
    public void checkAppointmentSuccessfulCreateInService() {
        Appointment app = Appointment.builderWithoutRelations().id(null).fromTime(time1).toTime(time3).build();
        Appointment appWithId = Appointment.builderWithoutRelations().id(5L).fromTime(app.getFromTime()).toTime(app.getToTime()).build();

        when(appointmentRepository.save(app)).thenReturn(appWithId);
        assertThat(appointmentService.create(app))
                .as("Service appointment create should return created entity %s", appWithId)
                .isEqualTo(appWithId);
        verify(appointmentRepository).save(app);
    }

    @Test
    public void checkAppointmentCreateExceptionWhenFromTimeIsNull() {
        Appointment app = Appointment.builderWithoutRelations().id(null).fromTime(null).toTime(time2).build();

        assertThatExceptionOfType(NonNullableFieldIsNullException.class)
                .as("Service appointment create should throw exception when appointment fromTime is null")
                .isThrownBy(() -> appointmentService.create(app))
                .withMessage("Non nullable field is null");
    }

    @Test
    public void checkAppointmentCreateExceptionWhenToTimeIsNull() {
        Appointment app = Appointment.builderWithoutRelations().id(null).fromTime(time3).toTime(null).build();

        assertThatExceptionOfType(NonNullableFieldIsNullException.class)
                .as("Service appointment create should throw exception when appointment toTime is null")
                .isThrownBy(() -> appointmentService.create(app))
                .withMessage("Non nullable field is null");
    }

    @Test
    public void checkAppointmentCreateExceptionWhenBothFromTimeAndToTimeAreNull() {
        Appointment app = Appointment.builderWithoutRelations().id(null).fromTime(null).toTime(null).build();

        assertThatExceptionOfType(NonNullableFieldIsNullException.class)
                .as("Service appointment create should throw exception when appointment fromTime and toTime are null")
                .isThrownBy(() -> appointmentService.create(app))
                .withMessage("Non nullable field is null");
    }

    @Test
    public void checkAppointmentCreateExceptionWhenFromTimeIsAfterToTime() {
        Appointment app = Appointment.builderWithoutRelations().id(null).fromTime(time3).toTime(time2).build();

        assertThatExceptionOfType(EndTimeBeforeStartTimeException.class)
                .as("Service appointment create should throw exception when appointment fromTime is after toTime")
                .isThrownBy(() -> appointmentService.create(app))
                .withMessage("Start time must be before end time");
    }

    @Test
    public void checkAppointmentCreateExceptionWhenFromTimeEqualsToTime() {
        Appointment app = Appointment.builderWithoutRelations().id(null).fromTime(time1).toTime(time1).build();

        assertThatExceptionOfType(EndTimeBeforeStartTimeException.class)
                .as("Service appointment create should throw exception when appointment fromTime equals toTime")
                .isThrownBy(() -> appointmentService.create(app))
                .withMessage("Start time must be before end time");
    }

    @Test
    public void checkAppointmentCreateSuccessWhenFromTimeIsBeforeToTimeBy1NanoSecond() {
        Appointment app = Appointment.builderWithoutRelations().id(null).fromTime(time3).toTime(time3.plusNanos(1)).build();

        assertThatCode(() -> appointmentService.create(app))
                .as("Service appointment create should throw exception when appointment fromTime is before toTime by 1 nanosecond")
                .doesNotThrowAnyException();
    }

    @Test
    public void checkAppointmentSuccessfulReadInService() {
        Appointment app = Appointment.builderWithoutRelations().id(4L).fromTime(time1).toTime(time2).build();

        when(appointmentRepository.findById(4L)).thenReturn(Optional.of(app));
        assertThat(appointmentService.readById(4L))
                .as("Service appointment read should return entity %s", app)
                .isEqualTo(Optional.of(app));
        verify(appointmentRepository).findById(4L);
    }

    @Test
    public void checkAppointmentReadExceptionWhenEntityDoesNotExist() {
        when(appointmentRepository.findById(4L)).thenReturn(Optional.empty());
        assertThat(appointmentService.readById(4L))
                .as("Service appointment read should return empty optional")
                .isEqualTo(Optional.empty());
    }

    @Test
    public void checkAppointmentSuccessfulReadAll() {
        Appointment app1 = Appointment.builderWithoutRelations().id(2L).fromTime(time1).toTime(time2).build();
        Appointment app2 = Appointment.builderWithoutRelations().id(3L).fromTime(time2).toTime(time3).build();
        Appointment app3 = Appointment.builderWithoutRelations().id(6L).fromTime(time1).toTime(time3).build();
        List<Appointment> apps = List.of(app1, app2, app3);

        when(appointmentRepository.findAll()).thenReturn(apps);
        assertThat(appointmentService.readAll())
                .as("Service appointment read all should return entities %s", apps)
                .isEqualTo(apps);
        verify(appointmentRepository).findAll();
    }

    @Test
    public void checkAppointmentDeleteExistingEntity() {
        Appointment app = Appointment.builderWithoutRelations().id(6L).fromTime(time1).toTime(time3).build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(app));
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        appointmentService.deleteById(1L);
        verify(appointmentRepository).deleteById(1L);
    }

    @Test
    public void checkAppointmentDeleteNonExistingEntity() {
        when(appointmentRepository.findById(1001L)).thenReturn(Optional.empty());
        when(appointmentRepository.existsById(1001L)).thenReturn(false);
        assertThatExceptionOfType(DeletingNonExistingEntityException.class)
                .as("Service appointment delete should throw exception when appointment does not exist")
                .isThrownBy(() -> appointmentService.deleteById(1001L))
                .withMessage("Attempt to delete non-existing entity");
    }

    @Test
    public void checkAppointmentSuccessfulUpdateInService() {
        Appointment app = Appointment.builderWithoutRelations().id(4L).fromTime(time1).toTime(time2).build();
        Appointment updatedApp = Appointment.builderWithoutRelations().id(app.getId()).fromTime(time2).toTime(time3).build();
        Appointment updatedAppWithRelations = Appointment.builderWithoutRelations()
                .id(updatedApp.getId()).fromTime(updatedApp.getFromTime()).toTime(updatedApp.getToTime()).build();
        Doctor doc = Doctor.builderWithoutRelations().id(7L).name("None").position("Some").build();
        PatientCase pat = PatientCase.builderWithoutRelations().id(9L).patientName("An").problem("The").build();
        app.setDoctor(doc);
        app.setPatientCase(pat);
        updatedAppWithRelations.setDoctor(doc);
        updatedAppWithRelations.setPatientCase(pat);

        when(appointmentRepository.findById(4L)).thenReturn(Optional.of(app));
        when(appointmentRepository.save(updatedAppWithRelations)).thenReturn(updatedAppWithRelations);
        assertThat(appointmentService.update(updatedApp))
                .as("Service appointment update should return entity %s", updatedAppWithRelations)
                .isEqualTo(updatedAppWithRelations);
        verify(appointmentRepository).save(updatedAppWithRelations);
    }

    @Test
    public void checkAppointmentUpdateExceptionWhenToTimeIsNull() {
        Appointment updatedApp = Appointment.builderWithoutRelations().id(4L).fromTime(time3).toTime(null).build();

        assertThatExceptionOfType(NonNullableFieldIsNullException.class)
                .as("Service appointment update should throw exception when toTime is null")
                .isThrownBy(() -> appointmentService.update(updatedApp))
                .withMessage("Non nullable field is null");
    }

    @Test
    public void checkAppointmentUpdateExceptionWhenFromTimeIsAfterToTime() {
        Appointment updatedApp = Appointment.builderWithoutRelations().id(6L).fromTime(time3).toTime(time1).build();

        assertThatExceptionOfType(EndTimeBeforeStartTimeException.class)
                .as("Service appointment update should throw exception when fromTime is after toTime")
                .isThrownBy(() -> appointmentService.update(updatedApp))
                .withMessage("Start time must be before end time");
    }

    @Test
    public void checkAppointmentUpdateExceptionWhenEntityDoesNotExist() {
        Appointment updatedApp = Appointment.builderWithoutRelations().id(9L).fromTime(time1).toTime(time2).build();

        when(appointmentRepository.findById(9L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(EntityNotFoundException.class)
                .as("Service appointment update should throw exception when entity does not exist")
                .isThrownBy(() -> appointmentService.update(updatedApp))
                .withMessage("Entity not found");
    }

    @Test
    public void checkAppointmentUpdateExceptionWhenTimesForDoctorAreIntersecting() {
        Appointment app = Appointment.builderWithoutRelations().id(9L).fromTime(time1).toTime(time2).build();
        Appointment anotherApp = Appointment.builderWithoutRelations().id(10L).fromTime(time2).toTime(time3).build();
        Appointment updatedApp = Appointment.builderWithoutRelations().id(app.getId()).fromTime(time1).toTime(time3).build();
        Doctor doc = Doctor.builderWithoutRelations().id(5L).name("A").position("B").build();
        app.setDoctor(doc);
        anotherApp.setDoctor(doc);
        doc.setAppointments(Set.of(app, anotherApp));

        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(app));
        assertThatExceptionOfType(TimeIntervalsAreIntersectingException.class)
                .as("Service appointment update should throw exception when doctor has intersecting appointments after update")
                .isThrownBy(() -> appointmentService.update(updatedApp))
                .withMessage("Time intervals are intersecting");
    }

    @Test
    public void checkAppointmentUpdateExceptionWhenTwoTimeIntervalsForDoctorStartAndEndAtTheSameTime() {
        Appointment app = Appointment.builderWithoutRelations().id(9L).fromTime(time1).toTime(time2).build();
        Appointment anotherApp = Appointment.builderWithoutRelations().id(10L).fromTime(time3).toTime(time3.plusDays(1)).build();
        Appointment updatedApp = Appointment.builderWithoutRelations().id(app.getId()).fromTime(time2).toTime(time3).build();
        Doctor doc = Doctor.builderWithoutRelations().id(5L).name("A").position("B").build();
        app.setDoctor(doc);
        anotherApp.setDoctor(doc);
        doc.setAppointments(Set.of(app, anotherApp));

        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(app));
        assertThatCode(() -> appointmentService.update(updatedApp))
                .as("Service appointment update should succeed when doctor two appointments start and end at the same time")
                .doesNotThrowAnyException();
    }
}
