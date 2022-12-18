package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.hospital_appointments.api.converter.DoctorConverter;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.business.DoctorService;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
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

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.DoctorConverter.toDto;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper jsonMapper;
    @MockBean private DoctorService doctorService;

    private final Doctor doc1 = Doctor.builderWithoutRelations().id(1L).name("The").position("name").build();
    private final Doctor doc2 = Doctor.builderWithoutRelations().id(2L).name("is").position("doctor").build();
    private final Doctor doc3 = Doctor.builderWithoutRelations().id(3L).name("dis").position("respect").build();
    private final List<Doctor> allDocs = List.of(doc1, doc2, doc3);

    @BeforeEach
    public void prepare() {
        when(doctorService.readById(1L)).thenReturn(Optional.of(doc1));
        when(doctorService.readById(2L)).thenReturn(Optional.of(doc2));
        when(doctorService.readById(3L)).thenReturn(Optional.of(doc3));
        when(doctorService.readAll()).thenReturn(allDocs);
    }

    @Test
    public void checkDoctorSuccessfulCreateReturnsCorrectData() throws Exception {
        Doctor createdDoc = Doctor.builderWithoutRelations().id(null).name("A").position("NotA").build();
        Doctor createdDocWithId = Doctor.builderWithoutRelations().id(4L).name(createdDoc.getName()).position(createdDoc.getPosition()).build();

        when(doctorService.create(createdDoc)).thenReturn(createdDocWithId);
        mockMvc.perform(post("/doctors")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(createdDoc))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(createdDocWithId))));
    }

    @Test
    public void checkDoctorSuccessfulReadReturnsCorrectData() throws Exception {
        mockMvc.perform(get("/doctors/2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(doc2))));
    }

    @Test
    public void checkDoctorReadErrorMessageWhenIdDoesNotExist() throws Exception {
        when(doctorService.readById(1001L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/doctors/1001")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    public void checkAllDoctorsReadReturnsCorrectData() throws Exception {
        List<DoctorDto> allDocDto = allDocs.stream().map(DoctorConverter::toDto).toList();

        mockMvc.perform(get("/doctors")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(allDocDto)));
    }

    @Test
    public void checkDoctorSuccessfulUpdateReturnsCorrectData() throws Exception {
        Doctor updatedDoc3 = Doctor.builderWithoutRelations().id(3L).name("Changed").position("Data").build();

        when(doctorService.update(updatedDoc3)).thenReturn(updatedDoc3);
        mockMvc.perform(put("/doctors/3")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedDoc3))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(jsonMapper.writeValueAsString(toDto(updatedDoc3))));
    }

    @Test
    public void checkDoctorUpdateErrorMessageWhenIdDoesNotExist() throws Exception {
        Doctor updatedDoc = Doctor.builderWithoutRelations().id(1001L).name("One").position(null).build();

        when(doctorService.update(updatedDoc)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(put("/doctors/1001")
                        .contentType(APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(toDto(updatedDoc))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Entity not found"));
    }

    @Test
    public void checkDoctorDeleteResponseForExistingEntity() throws Exception {
        doNothing().when(doctorService).deleteById(1L);
        mockMvc.perform(delete("/doctors/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void checkDoctorDeleteResponseForNonExistingEntity() throws Exception {
        doThrow(DeletingNonExistingEntityException.class).when(doctorService).deleteById(1001L);
        mockMvc.perform(delete("/doctors/1001")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
