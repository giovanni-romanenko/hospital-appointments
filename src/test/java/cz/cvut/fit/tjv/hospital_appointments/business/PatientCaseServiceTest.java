package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.dao.PatientCaseJpaRepository;
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
public class PatientCaseServiceTest {

    @Autowired private PatientCaseService patientCaseService;
    @MockBean private PatientCaseJpaRepository patientCaseRepository;

    private final LocalDateTime time1 = LocalDateTime.of(2000, 3, 4, 5, 6, 7);
    private final LocalDateTime time2 = LocalDateTime.of(2001, 4, 5, 6, 7, 8);

    @Test
    public void checkPatientCaseSuccessfulCreateInService() {
        PatientCase pat = PatientCase.builderWithoutRelations().id(null).patientName("Name").problem("Problem").build();
        PatientCase patWithId = PatientCase.builderWithoutRelations().id(5L).patientName(pat.getPatientName()).problem(pat.getProblem()).build();

        when(patientCaseRepository.save(pat)).thenReturn(patWithId);
        assertThat(patientCaseService.create(pat))
                .as("Service patient case create should return created entity %s", patWithId)
                .extracting(PatientCase::getId, PatientCase::getPatientName, PatientCase::getProblem)
                .containsExactly(patWithId.getId(), patWithId.getPatientName(), patWithId.getProblem());
        verify(patientCaseRepository).save(pat);
    }

    @Test
    public void checkPatientCaseSuccessfulReadInService() {
        PatientCase pat = PatientCase.builderWithoutRelations().id(4L).patientName("PN").problem("PB").build();

        when(patientCaseRepository.findById(4L)).thenReturn(Optional.of(pat));
        assertThat(patientCaseService.readById(4L))
                .as("Service patient case read should return entity %s", pat)
                .get()
                .extracting(PatientCase::getId, PatientCase::getPatientName, PatientCase::getProblem)
                .containsExactly(pat.getId(), pat.getPatientName(), pat.getProblem());
        verify(patientCaseRepository).findById(4L);
    }

    @Test
    public void checkPatientCaseReadExceptionWhenEntityDoesNotExist() {
        when(patientCaseRepository.findById(4L)).thenReturn(Optional.empty());
        assertThat(patientCaseService.readById(4L))
                .as("Service patient case read should return empty optional")
                .isEqualTo(Optional.empty());
    }

    @Test
    public void checkPatientCaseSuccessfulReadAll() {
        PatientCase pat1 = PatientCase.builderWithoutRelations().id(2L).patientName("1").problem("2").build();
        PatientCase pat2 = PatientCase.builderWithoutRelations().id(3L).patientName("3").problem("4").build();
        PatientCase pat3 = PatientCase.builderWithoutRelations().id(6L).patientName("5").problem("6").build();
        List<PatientCase> pats = List.of(pat1, pat2, pat3);

        when(patientCaseRepository.findAll()).thenReturn(pats);
        assertThat(patientCaseService.readAll())
                .as("Service patient case read all should return entities %s", pats)
                .extracting(PatientCase::getId, PatientCase::getPatientName, PatientCase::getProblem)
                .containsExactlyElementsOf(pats.stream().map(pat -> tuple(pat.getId(), pat.getPatientName(), pat.getProblem())).toList());
        verify(patientCaseRepository).findAll();
    }

    @Test
    public void checkPatientCaseDeleteExistingEntity() {
        PatientCase pat = PatientCase.builderWithoutRelations().id(6L).patientName("gl").problem("hf").build();

        when(patientCaseRepository.findById(1L)).thenReturn(Optional.of(pat));
        when(patientCaseRepository.existsById(1L)).thenReturn(true);
        patientCaseService.deleteById(1L);
        verify(patientCaseRepository).deleteById(1L);
    }

    @Test
    public void checkPatientCaseDeleteNonExistingEntity() {
        when(patientCaseRepository.findById(1001L)).thenReturn(Optional.empty());
        when(patientCaseRepository.existsById(1001L)).thenReturn(false);
        assertThatExceptionOfType(DeletingNonExistingEntityException.class)
                .as("Service patient case delete should throw exception when patient case does not exist")
                .isThrownBy(() -> patientCaseService.deleteById(1001L))
                .withMessage("Attempt to delete non-existing entity");
    }

    @Test
    public void checkPatientCaseSuccessfulUpdateInService() {
        PatientCase pat = PatientCase.builderWithoutRelations().id(4L).patientName("test").problem("test2").build();
        PatientCase updatedPat = PatientCase.builderWithoutRelations().id(pat.getId()).patientName("non-test").problem(null).build();
        PatientCase updatedPatWithRelations = PatientCase.builderWithoutRelations()
                .id(updatedPat.getId()).patientName(updatedPat.getPatientName()).problem(updatedPat.getProblem()).build();
        Doctor doc = Doctor.builderWithoutRelations().id(7L).name("None").position("Some").build();
        Appointment app = Appointment.builderWithoutRelations().id(11L).fromTime(time1).toTime(time2).build();
        pat.setQualifiedDoctors(Set.of(doc));
        pat.setAppointment(app);
        doc.setTreatablePatientCases(Set.of(pat));
        app.setPatientCase(pat);
        updatedPatWithRelations.setQualifiedDoctors(Set.of(doc));
        updatedPatWithRelations.setAppointment(app);

        when(patientCaseRepository.findById(4L)).thenReturn(Optional.of(pat));
        when(patientCaseRepository.save(updatedPatWithRelations)).thenReturn(updatedPatWithRelations);
        assertThat(patientCaseService.update(updatedPat))
                .as("Service patient case update should return entity %s", updatedPatWithRelations)
                .extracting(
                        PatientCase::getId,
                        PatientCase::getProblem,
                        PatientCase::getPatientName,
                        PatientCase::getAppointment,
                        PatientCase::getQualifiedDoctors)
                .containsExactly(
                        updatedPatWithRelations.getId(),
                        updatedPatWithRelations.getProblem(),
                        updatedPatWithRelations.getPatientName(),
                        updatedPatWithRelations.getAppointment(),
                        updatedPatWithRelations.getQualifiedDoctors());
        verify(patientCaseRepository).save(updatedPatWithRelations);
    }

    @Test
    public void checkPatientCaseUpdateExceptionWhenEntityDoesNotExist() {
        PatientCase updatedPat = PatientCase.builderWithoutRelations().id(9L).patientName("Yes").problem("No").build();

        when(patientCaseRepository.findById(9L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(EntityNotFoundException.class)
                .as("Service patient case update should throw exception when entity does not exist")
                .isThrownBy(() -> patientCaseService.update(updatedPat))
                .withMessage("Entity not found");
    }
}
