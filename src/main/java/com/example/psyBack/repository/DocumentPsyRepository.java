package com.example.psyBack.repository;

import com.example.psyBack.entity.DocumentPsy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentPsyRepository extends JpaRepository<DocumentPsy, Integer> {
}
