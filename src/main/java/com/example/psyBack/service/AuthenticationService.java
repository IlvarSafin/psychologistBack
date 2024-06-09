package com.example.psyBack.service;

import com.example.psyBack.dto.*;
import com.example.psyBack.entity.*;
import com.example.psyBack.entity.enums.ERole;
import com.example.psyBack.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PsychologistRepository psychologistRepository;
    private final DocumentPsyRepository documentPsyRepository;
    private final AppointmentDayRepository appointmentDayRepository;
    private final MailSender mailSender;

    public JwtAuthenticationResponse signUpPatient(SignUpPatientRequest request)
    {
        Users user = getUserForSignUp(request);
        Patient patient = new Patient();
        patient.setName(request.getName());
        Patient savedPatient = patientRepository.save(patient);
        logger.info("Пациент с id = " + savedPatient.getId() + " успешно сохранен");


        if (user == null) {
            user = new Users(request.getEmail(), passwordEncoder.encode(request.getPassword()));
            user.setConfirmCode(UUID.randomUUID().toString());
            sendConfirmationEmail(user);
        }
        user.getRoles().add(ERole.ROLE_USER);
        user.setPatient(savedPatient);

        Users savedUser =  userRepository.save(user);
        logger.info("Пользователь с id = " + savedUser.getId() + " успешно сохранен");

        String jwt = jwtService.generateToken(savedUser);
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    public JwtAuthenticationResponse signUpPsychologist(SignUpPsychologistRequest request)
    {
        Users user = getUserForSignUp(request);



        if(request.getDocs().size() > 3)
        {
            logger.error("Загружать можно только 3 документа");
            throw new RuntimeException("Загружать можно только 3 документа");
        }

        Psychologist savedPsychologist;
        Psychologist psychologist = new Psychologist();
        psychologist.setAge(request.getAge());
        psychologist.setDescription(request.getDescription());
        psychologist.setName(request.getName());
        psychologist.setExperience(request.getExperience());
        psychologist.setSex(request.getSex());

        byte[] imageBytes = Base64.getDecoder().decode(request.getFacePhoto());
        psychologist.setPhoto(imageBytes);

        savedPsychologist = psychologistRepository.save(psychologist);
        logger.info("Психолог с id " + savedPsychologist.getId() + " успешно сохранен");

        List<AppointmentDay> appointmentDays = request
                .getAppointmentDays()
                .stream()
                .map(e -> new AppointmentDay(e.getDayOfWeek(), e.getDates(), savedPsychologist))
                .toList();
        appointmentDayRepository.saveAll(appointmentDays);
        logger.info("Дни для записи для психолога с id = " + savedPsychologist.getId() + " успешно сохранены");


        for (String doc : request.getDocs())
        {
            DocumentPsy documentPsy = new DocumentPsy();
            documentPsy.setDocument(Base64.getDecoder().decode(doc));
            documentPsy.setPsychologist(savedPsychologist);
            documentPsyRepository.save(documentPsy);
        }
        logger.info("Документы для регистрации психолога с id " + savedPsychologist.getId() + " успешно сохранены");
        if (user == null)
        {
            user = new Users(request.getEmail(), passwordEncoder.encode(request.getPassword()));
            user.setConfirmCode(UUID.randomUUID().toString());
            sendConfirmationEmail(user);
        }
        user.getRoles().add(ERole.ROLE_PSY);
        user.setPsychologist(savedPsychologist);

        Users savedUser = userRepository.save(user);
        logger.info("Пользователь с id = " + savedUser.getId() + " успешно сохранен");

        String jwt = jwtService.generateToken(savedUser);
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    public JwtAuthenticationResponse signIn(SignInRequest request)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getEmail());
        logger.info("Пользователь с email = " + user.getUsername() + " успешно вощел в систему");

        String token = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(token);
    }

    private Users getUserForSignUp(SignUpPatientRequest request)
    {
        Users user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (request.getConfirmedPassword().isEmpty())
        {
            if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword()))
            {
                logger.error("По такому паролю и емайлу пользователя в системе нет");
                throw new RuntimeException("По такому паролю и емайлу пользователя в системе нет");
            }
        } else if (!request.getConfirmedPassword().equals(request.getPassword()) || user != null)
        {
            logger.error("Такой пользователь уже есть или пороль неправильный");
            throw new RuntimeException("Такой пользователь уже есть или пороль неправильный");
        }

        return user;
    }

    private void sendConfirmationEmail(Users user)
    {
        String subject = "Подтверждение Email";
        String text = "Пожалуйста, перейдите по ссылке, чтобы активировать аккаунт в сервисе СПРОСИ: " +
                "http://localhost:8080/api/v1/auth/confirm?code=" + user.getConfirmCode();
//        String subject = "Email";
//        String text = "http://localhost:8080/api/v1/auth/confirm?code=" + user.getConfirmCode();
        mailSender.send(user.getEmail(), subject, text);
    }

    public boolean confirmEmail(String code) {
        Users user = userRepository.findByConfirmCode(code)
                .orElse(null);

        if (user == null) {
            return false;
        }

        user.setStatus(true);
        userRepository.save(user);
        return true;
    }
}
