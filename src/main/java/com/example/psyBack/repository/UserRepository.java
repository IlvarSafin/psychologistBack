package com.example.psyBack.repository;

import com.example.psyBack.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Transactional
    Optional<Users> findByEmail(String email);

    @Transactional
    Optional<Users> findByConfirmCode(String code);
}
