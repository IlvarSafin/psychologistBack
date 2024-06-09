package com.example.psyBack.repository;

import com.example.psyBack.entity.Appointment;
import com.example.psyBack.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatient(Patient patient);
}
