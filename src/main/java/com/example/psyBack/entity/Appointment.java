package com.example.psyBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "appointment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "date")
    private Date date;
    @Column(name = "status")
    private boolean status;
    @Column(name = "link")
    private String link;

    @OneToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private Review review;

    @ManyToOne()
    @JoinColumn(name = "psychologist_id", referencedColumnName = "id")
    private Psychologist psychologist;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;
}
