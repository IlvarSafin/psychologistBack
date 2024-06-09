package com.example.psyBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDayDTO {
    private DayOfWeek dayOfWeek;
    private List<LocalTime> dates;
}
