package com.example.psyBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "document_psy")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentPsy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Lob
    @Column(name = "document")
    private byte[] document;

    @ManyToOne
    @JoinColumn(name = "psy_id", referencedColumnName = "id")
    private Psychologist psychologist;
}
