package com.example.psyBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "psychologist")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Psychologist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @Column(name = "status")
    private boolean status;
    @Column(name = "description")
    private String description;
    @Column(name = "sex")
    private char sex;
    @Column(name = "price")
    private double price;
    @Column(name = "experience")
    private double experience;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @OneToOne(mappedBy = "psychologist")
    private Users users;

    @OneToMany(mappedBy = "psychologist")
    private List<Review> reviews;

    @OneToMany(mappedBy = "psychologist")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "psychologist")
    private List<AppointmentDay> appointmentDays;

    @OneToMany(mappedBy = "psychologist", fetch = FetchType.LAZY)
    private List<DocumentPsy> documentPsyList;

    @ElementCollection(targetClass = Specialization.class)
    @CollectionTable(name = "psuchologist_filter",
            joinColumns = @JoinColumn(name = "psychologist_id"))
    private Set<Specialization> specializations = new HashSet<>();
}
