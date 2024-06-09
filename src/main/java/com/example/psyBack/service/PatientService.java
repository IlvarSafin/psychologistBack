package com.example.psyBack.service;

import com.example.psyBack.dto.PatientCreateDto;
import com.example.psyBack.entity.Patient;
import com.example.psyBack.entity.Users;
import com.example.psyBack.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    private UserService userService;

    @Autowired
    public PatientService(
            UserService userService,
            PatientRepository patientRepository
    )
    {
        this.userService = userService;
        this.patientRepository = patientRepository;
    }

    public Patient findById(int id) throws Exception
    {
        return patientRepository.findById(id).orElseThrow(() -> new Exception("Not fount patient by id = " + id));
    }

    public Patient createNew(PatientCreateDto dto)
    {
        Patient patient = new Patient();
        patient.setName(dto.getName());

        Patient savedPatient = patientRepository.save(patient);
        userService.addPatient(patient);

        return savedPatient;
    }
}
