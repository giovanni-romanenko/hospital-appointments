package cz.cvut.fit.tjv.hospital_appointments.dao;

import cz.cvut.fit.tjv.hospital_appointments.domain.Appointment;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DoctorJpaRepositoryTest {

    @Autowired private AppointmentJpaRepository appointmentJpaRepository;
    @Autowired private DoctorJpaRepository doctorJpaRepository;
    @Autowired private PatientCaseJpaRepository patientCaseJpaRepository;

    private final LocalDateTime time1 = LocalDateTime.of(2000, 3, 4, 5, 6, 7);
    private final LocalDateTime time2 = LocalDateTime.of(2001, 4, 5, 6, 7, 8);
    private final LocalDateTime time3 = LocalDateTime.of(2002, 10, 10, 10, 10, 10);
    private final LocalDateTime time4 = LocalDateTime.of(2003, 11, 11, 11, 11, 11);
    private final LocalDateTime time5 = LocalDateTime.of(2004, 5, 4, 6, 3, 2);
    private final LocalDateTime time6 = LocalDateTime.of(2005, 1, 1, 0, 9, 1);

    private final Appointment app1 = Appointment.builderWithoutRelations().id(1L).fromTime(time1).toTime(time2).build();
    private final Appointment app2 = Appointment.builderWithoutRelations().id(2L).fromTime(time3).toTime(time4).build();
    private final Appointment app3 = Appointment.builderWithoutRelations().id(3L).fromTime(time5).toTime(time6).build();
    private final Doctor doc1 = Doctor.builderWithoutRelations().id(4L).name("John").position("Medic").build();
    private final Doctor doc2 = Doctor.builderWithoutRelations().id(5L).name("Amy").position("Physician").build();
    private final Doctor doc3 = Doctor.builderWithoutRelations().id(6L).name("Bob").position("Doc").build();
    private final PatientCase pat1 = PatientCase.builderWithoutRelations().id(7L).patientName("Kate").problem("Sick").build();
    private final PatientCase pat2 = PatientCase.builderWithoutRelations().id(8L).patientName("Ash").problem("Head").build();
    private final PatientCase pat3 = PatientCase.builderWithoutRelations().id(9L).patientName("Kevin").problem("Leg").build();

    @AfterEach
    public void tearDown() {
        // todo - google how to properly save and remove entities in datajpatest
        removeAllEntityRelations();
        appointmentJpaRepository.deleteAll();
        doctorJpaRepository.deleteAll();
        patientCaseJpaRepository.deleteAll();
    }

    @Test
    public void checkFindAllDoctorsWhoHaveAppointmentsWithPatientCasesThatTheyCanNotWorkOn1() {
        saveEntities();
        app1.setPatientCase(pat1);
        app2.setPatientCase(pat2);
        app3.setPatientCase(pat3);
        app1.setDoctor(doc1);
        app2.setDoctor(doc2);
        app3.setDoctor(doc3);
        pat1.setQualifiedDoctors(Set.of(doc2, doc3));
        pat2.setQualifiedDoctors(Set.of(doc1, doc2));
        pat3.setQualifiedDoctors(Set.of(doc3));
        saveEntities();
        assertThat(doctorJpaRepository.findAllDoctorsWhoHaveAppointmentsWithPatientCasesThatTheyCanNotWorkOn())
                .as("Find doctors breaking IC3 should return %s", List.of(doc1))
                .isEqualTo(List.of(doc1));
    }

    @Test
    public void checkFindAllDoctorsWhoHaveAppointmentsWithPatientCasesThatTheyCanNotWorkOn2() {
        saveEntities();
        app1.setPatientCase(pat1);
        app2.setPatientCase(pat2);
        app3.setPatientCase(pat3);
        app1.setDoctor(doc1);
        app2.setDoctor(doc2);
        app3.setDoctor(doc3);
        pat1.setQualifiedDoctors(Set.of(doc2, doc3));
        pat2.setQualifiedDoctors(Set.of(doc1, doc2));
        pat3.setQualifiedDoctors(Set.of(doc3));
        saveEntities();
        assertThat(doctorJpaRepository.findAllDoctorsWhoHaveAppointmentsWithPatientCasesThatTheyCanNotWorkOn())
                .as("Find doctors breaking IC3 should return %s", List.of(doc1))
                .isEqualTo(List.of(doc1));
    }

    @Test
    public void checkFindAllDoctorsWhoHaveAppointmentsWithPatientCasesThatTheyCanNotWorkOn3() {
        saveEntities();
        app1.setPatientCase(pat1);
        app2.setPatientCase(pat2);
        app3.setPatientCase(pat3);
        app1.setDoctor(doc1);
        app2.setDoctor(doc2);
        app3.setDoctor(doc3);
        pat1.setQualifiedDoctors(Set.of(doc2, doc3));
        pat2.setQualifiedDoctors(Set.of(doc1, doc2));
        pat3.setQualifiedDoctors(Set.of(doc3));
        saveEntities();
        assertThat(doctorJpaRepository.findAllDoctorsWhoHaveAppointmentsWithPatientCasesThatTheyCanNotWorkOn())
                .as("Find doctors breaking IC3 should return %s", List.of(doc1))
                .isEqualTo(List.of(doc1));
    }

    private void saveEntities() {
        appointmentJpaRepository.saveAll(List.of(app1, app2, app3));
        doctorJpaRepository.saveAll(List.of(doc1, doc2, doc3));
        patientCaseJpaRepository.saveAll(List.of(pat1, pat2, pat3));
    }

    private void removeAllEntityRelations() {
        appointmentJpaRepository.findAll().forEach(appointment -> {
            appointment.setDoctor(null);
            appointment.setPatientCase(null);
        });
        patientCaseJpaRepository.findAll().forEach(patientCase ->
                patientCase.setQualifiedDoctors(Set.of()));
    }
}
