package com.example.psyBack.controller;

import com.example.psyBack.dto.AppointmentPatientDto;
import com.example.psyBack.dto.AppointmentPsyDto;
import com.example.psyBack.dto.PsychologistDTO;
import com.example.psyBack.entity.Appointment;
import com.example.psyBack.entity.PsychologistChanges;
import com.example.psyBack.service.AppointmentService;
import com.example.psyBack.service.PsychologistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/psychologist")
@RequiredArgsConstructor
public class PsychologistController {
    private final AppointmentService appointmentService;
    private final PsychologistService psychologistService;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentPsyDto>> getActiveAppointments()
    {
        return ResponseEntity.ok(appointmentService.psyActiveAppointments());
    }

    @PostMapping("/setLink")
    public ResponseEntity<AppointmentPsyDto> setAppointmentLink(@RequestBody AppointmentPsyDto appointmentPsyDto) {
        Appointment appointment = appointmentService.setLink(appointmentPsyDto);
        return appointment != null ? ResponseEntity.ok(new AppointmentPsyDto(
                appointment.getId(),
                appointment.getPatient().getName(),
                appointment.getDate(),
                appointment.getLink()
        )) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/edit")
    public ResponseEntity<Boolean> editPsyInfo(@RequestBody PsychologistDTO psychologistDTO) {
        return ResponseEntity.ok(psychologistService.editPsy(psychologistDTO));
    }
}
