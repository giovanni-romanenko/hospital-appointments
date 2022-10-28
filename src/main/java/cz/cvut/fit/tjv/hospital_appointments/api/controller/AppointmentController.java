package cz.cvut.fit.tjv.hospital_appointments.api.controller;

import cz.cvut.fit.tjv.hospital_appointments.api.dto.AppointmentDto;
import cz.cvut.fit.tjv.hospital_appointments.business.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments/{id}")
    public AppointmentDto readById(@PathVariable Long id) {
        return // todo
    }

    @GetMapping("/appointments")
    public Collection<AppointmentDto> readAll() {
        return // todo
    }

    @PostMapping("/appointments")
    public AppointmentDto create() { // todo
        // todo
    }

    @PutMapping("/appointments/{id}")
    public AppointmentDto update(@PathVariable Long id, ) { // todo
        // todo
    }

    @DeleteMapping("/appointments/{id}")
    public void deleteById(@PathVariable Long id) {
        appointmentService.deleteById(id);
    }
}
