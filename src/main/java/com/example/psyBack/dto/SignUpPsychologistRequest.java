package com.example.psyBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpPsychologistRequest extends SignUpPatientRequest{
    private int age;
    private String description;
    private char sex;
    private double experience;
    private double price;

    private List<AppointmentDayDTO> appointmentDays;

    private String facePhoto;
    private List<String> docs;
}
