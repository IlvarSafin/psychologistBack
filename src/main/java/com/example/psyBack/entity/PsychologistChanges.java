package com.example.psyBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "psychologist_changes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PsychologistChanges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "psychologist_id", referencedColumnName = "id")
    private Psychologist psychologist;
}
