package com.example.psyBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "appointment_day")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;
    @ElementCollection
    @Temporal(TemporalType.TIME)
    @Column(name = "dates")
    private List<LocalTime> dates;

    @ManyToOne
    @JoinColumn(name = "psychologist_id", referencedColumnName = "id")
    private Psychologist psychologist;

    public AppointmentDay(DayOfWeek dayOfWeek,
                          List<LocalTime> dates,
                          Psychologist psychologist)
    {
        this.dates = dates;
        this.dayOfWeek = dayOfWeek;
        this.psychologist = psychologist;
    }
}
