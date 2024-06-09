package com.example.psyBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "text")
    private String text;
    @Column(name = "is_liked")
    private boolean isLiked;
    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne()
    @JoinColumn(name = "psy_id", referencedColumnName = "id")
    private Psychologist psychologist;
}
