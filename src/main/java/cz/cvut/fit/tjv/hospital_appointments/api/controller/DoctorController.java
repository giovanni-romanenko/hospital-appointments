package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.DoctorDto;
import cz.cvut.fit.tjv.hospital_appointments.business.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/doctors/{id}")
    public DoctorDto readById(@PathVariable Long id) {
        return // todo
    }

    @GetMapping("/doctors")
    public Collection<DoctorDto> readAll() {
        return // todo
    }

    @PostMapping("/doctors")
    public DoctorDto create() { // todo
        // todo
    }

    @PutMapping("/doctors/{id}")
    public DoctorDto update(@PathVariable Long id, ) { // todo
        // todo
    }

    @DeleteMapping("/doctors/{id}")
    public void deleteById(@PathVariable Long id) {
        doctorService.deleteById(id);
    }
}
