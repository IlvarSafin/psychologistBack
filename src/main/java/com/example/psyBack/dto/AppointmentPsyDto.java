package com.example.psyBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentPsyDto {
    private int id;
    private String patientName;
    private Date date;
    private String link;
}
