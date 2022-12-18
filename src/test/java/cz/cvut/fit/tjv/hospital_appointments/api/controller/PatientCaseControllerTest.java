package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.PatientCaseConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.PatientCaseDto;
import cz.cvut.fit.tjv.hospital_appointments.business.PatientCaseService;
import cz.cvut.fit.tjv.hospital_appointments.domain.PatientCase;
import cz.cvut.fit.tjv.hospital_appointments.exception.DeletingNonExistingEntityException;
import cz.cvut.fit.tjv.hospital_appointments.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.PatientCaseConverter.toDto;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientCaseController.class)
public class PatientCaseControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper jsonMapper;
    @MockBean private PatientCaseService patientCaseService;

    private final PatientCase pat1 = PatientCase.builderWithoutRelations().id(1L).patientName("qw").problem("er").build();
    private final PatientCase pat2 = PatientCase.builderWithoutRelations().id(2L).patientName("ty").problem("ui").build();
    private final PatientCase pat3 = PatientCase.builderWithoutRelations().id(3L).patientName("op").problem("as").build();
    private final List<PatientCase> allPats = List.of(pat1, pat2, pat3);

    @BeforeEach
    public void prepare() {
        when(patientCaseService.readById(1L)).thenReturn(Optional.of(pat1));
        when(patientCaseService.readById(2L)).thenReturn(Optional.of(pat2));
        when(patientCaseService.readById(3L)).thenReturn(Optional.of(pat3));
        when(patientCaseService.readAll()).thenReturn(allPats);
    }

    @Test
    public void checkPatientCaseSuccessfulCreateReturnsCorrectData() throws Exception {
        PatientCase createdPat = PatientCase.builderWithoutRelations().id(null).patientName("Kln").problem("ton").build();
        PatientCase createdPatWithId = PatientCase.builderWithoutRelations().id(4L).patientName(createdPat.getPatientName()).problem(createdPat.getProblem()).build();

        when(patientCaseService.create(createdPat)).thenReturn(createdPatWithId);
        mockMvc.perform(post("/patient_cases")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(createdPat))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(createdPatWithId))));
    }

    @Test
    public void checkPatientCaseSuccessfulReadReturnsCorrectData() throws Exception {
        mockMvc.perform(get("/patient_cases/2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(pat2))));
    }

    @Test
    public void checkPatientCaseReadErrorMessageWhenIdDoesNotExist() throws Exception {
        when(patientCaseService.readById(1001L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/patient_cases/1001")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    public void checkAllPatientCaseReadReturnsCorrectData() throws Exception {
        List<PatientCaseDto> allPatDto = allPats.stream().map(PatientCaseConverter::toDto).toList();

        mockMvc.perform(get("/patient_cases")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(allPatDto)));
    }

    @Test
    public void checkPatientCaseSuccessfulUpdateReturnsCorrectData() throws Exception {
        PatientCase updatedPat3 = PatientCase.builderWithoutRelations().id(3L).patientName("PatName").problem("PatProblem").build();

        when(patientCaseService.update(updatedPat3)).thenReturn(updatedPat3);
        mockMvc.perform(put("/patient_cases/3")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedPat3))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(updatedPat3))));
    }

    @Test
    public void checkPatientCaseUpdateErrorMessageWhenIdDoesNotExist() throws Exception {
        PatientCase updatedPat = PatientCase.builderWithoutRelations().id(1001L).patientName(null).problem(null).build();

        when(patientCaseService.update(updatedPat)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(put("/patient_cases/1001")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedPat))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    public void checkPatientCaseDeleteResponseForExistingEntity() throws Exception {
        doNothing().when(patientCaseService).deleteById(1L);
        mockMvc.perform(delete("/patient_cases/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void checkPatientCaseDeleteResponseForNonExistingEntity() throws Exception {
        doThrow(DeletingNonExistingEntityException.class).when(patientCaseService).deleteById(1001L);
        mockMvc.perform(delete("/patient_cases/1001")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
