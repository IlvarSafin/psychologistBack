package com.example.psyBack.repository;

import com.example.psyBack.entity.AppointmentDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentDayRepository extends JpaRepository<AppointmentDay, Integer> {
}
