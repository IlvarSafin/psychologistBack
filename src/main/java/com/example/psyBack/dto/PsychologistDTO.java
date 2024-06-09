package com.example.psyBack.dto;

import com.example.psyBack.entity.Specialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PsychologistDTO {
    private int id;
    private String name;
    private String description;
    private int countReview;
    private char sex;
    private byte[] photo;
    private boolean allReviews;

    private Set<Specialization> specializations;
}
