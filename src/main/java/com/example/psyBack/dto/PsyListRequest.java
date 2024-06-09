package com.example.psyBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PsyListRequest {
    private List<PsychologistDTO> psychologists;
    private int maxPage;
}
