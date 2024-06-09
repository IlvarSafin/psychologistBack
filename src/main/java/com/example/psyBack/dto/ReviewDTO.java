package com.example.psyBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDTO {
    private int id;
    private String text;

    public ReviewDTO(String text)
    {
        this.text = text;
    }
}
