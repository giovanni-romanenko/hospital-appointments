package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.api.views.DoctorViews;
import cz.cvut.fit.tjv.hospital_appointments.business.DoctorService;
import cz.cvut.fit.tjv.hospital_appointments.domain.Doctor;
import cz.cvut.fit.tjv.hospital_appointments.exception.CreatedEntityNullIdException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

import static cz.cvut.fit.tjv.hospital_appointments.api.converter.DoctorConverter.*;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/doctors/{id}")
    public DoctorDto readById(@PathVariable Long id) {
        return toDto(doctorService.readById(id).orElseThrow(EntityNotFoundException::new));
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @GetMapping("/doctors")
    public Collection<DoctorDto> readAll() {
        return toDtoMany(doctorService.readAll());
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @PostMapping("/doctors")
    public DoctorDto create(
            @JsonView(DoctorViews.FullDataWithoutId.class)
            @RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorService.create(fromDto(doctorDto));
        if (doctor.getId() == null) {
            throw new CreatedEntityNullIdException(doctor);
        }
        return toDto(doctor);
    }

    @JsonView(DoctorViews.FullDataWithId.class)
    @PutMapping("/doctors/{id}")
    public DoctorDto update(
            @PathVariable Long id,
            @JsonView(DoctorViews.FullDataWithoutId.class)
            @RequestBody DoctorDto doctorDto) {
        Doctor doctor = fromDto(doctorDto);
        doctor.setId(id);
        return toDto(doctorService.update(doctor));
    }

    @DeleteMapping("/doctors/{id}")
    public void deleteById(@PathVariable Long id) {
        doctorService.deleteById(id);
    }
}
