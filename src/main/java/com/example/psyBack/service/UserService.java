package com.example.psyBack.service;

import com.example.psyBack.entity.Patient;
import com.example.psyBack.entity.Users;
import com.example.psyBack.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public Users changeStatus(int id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

        boolean currentStatus = user.isStatus();
        user.setStatus(!currentStatus);
        return userRepository.save(user);
    }

    public Users getByEmail(String email)
    {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService()
    {
        return this::getByEmail;
    }

    public Users getCurrentUser()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByEmail(email);
    }

    public Users addPatient(Patient patient)
    {
        Users user = getCurrentUser();
        user.setPatient(patient);
        Users savedUser = userRepository.save(user);
        logger.info("Пациент с id = " + patient.getId() + " добавлен к пользователю с id = " + savedUser.getId());
        return savedUser;
    }

}
