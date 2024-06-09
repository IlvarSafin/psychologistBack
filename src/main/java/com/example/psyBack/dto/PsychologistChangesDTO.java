package com.example.psyBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PsychologistChangesDTO {
    private int id;
    private String newDescription;
    private PsychologistDTO psychologist;
}
