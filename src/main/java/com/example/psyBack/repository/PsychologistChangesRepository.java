package com.example.psyBack.repository;

import com.example.psyBack.entity.Psychologist;
import com.example.psyBack.entity.PsychologistChanges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PsychologistChangesRepository extends JpaRepository<PsychologistChanges, Integer> {
    List<PsychologistChanges> findByPsychologist(Psychologist psychologist);
}
