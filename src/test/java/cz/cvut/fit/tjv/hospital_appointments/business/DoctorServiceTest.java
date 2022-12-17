package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.DoctorJpaRepository;
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
public class DoctorServiceTest {

    @Autowired private DoctorService doctorService;
    @MockBean private DoctorJpaRepository doctorRepository;

    private final LocalDateTime time1 = LocalDateTime.of(2000, 3, 4, 5, 6, 7);
    private final LocalDateTime time2 = LocalDateTime.of(2001, 4, 5, 6, 7, 8);

    @Test
    public void checkDoctorSuccessfulCreateInService() {
        Doctor doc = Doctor.builderWithoutRelations().id(null).name("M").position("Y").build();
        Doctor docWithId = Doctor.builderWithoutRelations().id(5L).name(doc.getName()).position(doc.getPosition()).build();

        when(doctorRepository.save(doc)).thenReturn(docWithId);
        assertThat(doctorService.create(doc))
                .as("Service doctor create should return created entity %s", docWithId)
                .extracting(Doctor::getId, Doctor::getName, Doctor::getPosition)
                .containsExactly(docWithId.getId(), docWithId.getName(), docWithId.getPosition());
        verify(doctorRepository).save(doc);
    }

    @Test
    public void checkDoctorSuccessfulReadInService() {
        Doctor doc = Doctor.builderWithoutRelations().id(4L).name("LL").position("123").build();

        when(doctorRepository.findById(4L)).thenReturn(Optional.of(doc));
        assertThat(doctorService.readById(4L))
                .as("Service doctor read should return entity %s", doc)
                .get()
                .extracting(Doctor::getId, Doctor::getName, Doctor::getPosition)
                .containsExactly(doc.getId(), doc.getName(), doc.getPosition());
        verify(doctorRepository).findById(4L);
    }

    @Test
    public void checkDoctorReadExceptionWhenEntityDoesNotExist() {
        when(doctorRepository.findById(4L)).thenReturn(Optional.empty());
        assertThat(doctorService.readById(4L))
                .as("Service doctor read should return empty optional")
                .isEqualTo(Optional.empty());
    }

    @Test
    public void checkDoctorSuccessfulReadAll() {
        Doctor doc1 = Doctor.builderWithoutRelations().id(2L).name("A").position("B").build();
        Doctor doc2 = Doctor.builderWithoutRelations().id(3L).name("C").position("D").build();
        Doctor doc3 = Doctor.builderWithoutRelations().id(6L).name("E").position("F").build();
        List<Doctor> docs = List.of(doc1, doc2, doc3);

        when(doctorRepository.findAll()).thenReturn(docs);
        assertThat(doctorService.readAll())
                .as("Service doctor read all should return entities %s", docs)
                .extracting(Doctor::getId, Doctor::getName, Doctor::getPosition)
                .containsExactlyElementsOf(docs.stream().map(doc -> tuple(doc.getId(), doc.getName(), doc.getPosition())).toList());
        verify(doctorRepository).findAll();
    }

    @Test
    public void checkAppointmentDeleteExistingEntity() {
        Doctor doc = Doctor.builderWithoutRelations().id(6L).name("John").position("Wick").build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doc));
        when(doctorRepository.existsById(1L)).thenReturn(true);
        doctorService.deleteById(1L);
        verify(doctorRepository).deleteById(1L);
    }

    @Test
    public void checkAppointmentDeleteNonExistingEntity() {
        when(doctorRepository.findById(1001L)).thenReturn(Optional.empty());
        when(doctorRepository.existsById(1001L)).thenReturn(false);
        assertThatExceptionOfType(DeletingNonExistingEntityException.class)
                .as("Service doctor delete should throw exception when doctor does not exist")
                .isThrownBy(() -> doctorService.deleteById(1001L))
                .withMessage("Attempt to delete non-existing entity");
    }

    @Test
    public void checkAppointmentSuccessfulUpdateInService() {
        Doctor doc = Doctor.builderWithoutRelations().id(4L).name("One").position("Two").build();
        Doctor updatedDoc = Doctor.builderWithoutRelations().id(doc.getId()).name("Three").position("Four").build();
        Doctor updatedDocWithRelations = Doctor.builderWithoutRelations().id(updatedDoc.getId()).name(updatedDoc.getName()).position(updatedDoc.getPosition()).build();
        Appointment app = Appointment.builderWithoutRelations().id(11L).fromTime(time1).toTime(time2).build();
        PatientCase pat = PatientCase.builderWithoutRelations().id(4L).patientName("test").problem("test2").build();
        doc.setAppointments(Set.of(app));
        doc.setTreatablePatientCases(Set.of(pat));
        app.setDoctor(doc);
        pat.setQualifiedDoctors(Set.of(doc));
        updatedDocWithRelations.setAppointments(Set.of(app));
        updatedDocWithRelations.setTreatablePatientCases(Set.of(pat));

        when(doctorRepository.findById(4L)).thenReturn(Optional.of(doc));
        when(doctorRepository.save(updatedDocWithRelations)).thenReturn(updatedDocWithRelations);
        assertThat(doctorService.update(updatedDoc))
                .as("Service doctor update should return entity %s", updatedDocWithRelations)
                .extracting(
                        Doctor::getId,
                        Doctor::getName,
                        Doctor::getPosition,
                        Doctor::getAppointments,
                        Doctor::getTreatablePatientCases)
                .containsExactly(
                        updatedDocWithRelations.getId(),
                        updatedDocWithRelations.getName(),
                        updatedDocWithRelations.getPosition(),
                        updatedDocWithRelations.getAppointments(),
                        updatedDocWithRelations.getTreatablePatientCases());
        verify(doctorRepository).save(updatedDocWithRelations);
    }

    @Test
    public void checkAppointmentUpdateExceptionWhenEntityDoesNotExist() {
        Doctor updatedDoc = Doctor.builderWithoutRelations().id(9L).name("First").position("Second").build();

        when(doctorRepository.findById(9L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(EntityNotFoundException.class)
                .as("Service doctor update should throw exception when entity does not exist")
                .isThrownBy(() -> doctorService.update(updatedDoc))
                .withMessage("Entity not found");
    }
}
