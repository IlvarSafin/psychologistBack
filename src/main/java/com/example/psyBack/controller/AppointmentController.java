package com.example.psyBack.controller;

import com.example.psyBack.dto.AppointmentCreateDto;
import com.example.psyBack.dto.AppointmentPatientDto;
import com.example.psyBack.dto.CloseAppointmentDTO;
import com.example.psyBack.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(
            AppointmentService appointmentService
    )
    {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/createAppointment")
    public ResponseEntity<Boolean> createAppointment(@RequestBody AppointmentCreateDto appointmentCreateDto) {
        try
        {
            appointmentService.saveNewAppointment(appointmentCreateDto);
            return ResponseEntity.ok(true);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<List<AppointmentPatientDto>> currentAppointments() {
        List<AppointmentPatientDto> res = appointmentService.patientActiveAppointments();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/closed")
    public ResponseEntity<List<AppointmentPatientDto>> finishedAppointments() {
        List<AppointmentPatientDto> res = appointmentService.patientClosedAppointments();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/close")
    public ResponseEntity<Boolean> closeAppointment(@RequestBody CloseAppointmentDTO closeAppointmentDTO) {
        if(appointmentService.close(closeAppointmentDTO) != null)
        {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ofNullable(false);
    }
}
