package com.example.psyBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CloseAppointmentDTO {
    private int appointmentId;
    private boolean liked;
    private String reviewText;
}
