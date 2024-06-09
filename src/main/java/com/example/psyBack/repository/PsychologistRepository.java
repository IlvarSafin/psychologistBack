package com.example.psyBack.repository;

import com.example.psyBack.entity.Psychologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsychologistRepository extends JpaRepository<Psychologist, Integer> {

}
