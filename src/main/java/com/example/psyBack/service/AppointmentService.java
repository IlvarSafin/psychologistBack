package com.example.psyBack.service;

import com.example.psyBack.dto.AppointmentCreateDto;
import com.example.psyBack.dto.AppointmentPatientDto;
import com.example.psyBack.dto.AppointmentPsyDto;
import com.example.psyBack.dto.CloseAppointmentDTO;
import com.example.psyBack.entity.*;
import com.example.psyBack.repository.AppointmentRepository;
import com.example.psyBack.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    private AppointmentRepository appointmentRepository;
    private PsychologistService psychologistService;
    private PatientService patientService;
    private ReviewRepository reviewRepository;
    private UserService userService;

    @Autowired
    public AppointmentService(
            ReviewRepository reviewRepository,
            PsychologistService psychologistService,
            PatientService patientService,
            AppointmentRepository appointmentRepository,
            UserService userService
    )
    {
        this.reviewRepository = reviewRepository;
        this.psychologistService = psychologistService;
        this.patientService = patientService;
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
    }

    public Appointment setLink(AppointmentPsyDto appointmentPsyDto) {
        Appointment appointment = findById(appointmentPsyDto.getId());
        if (appointment == null)
        {
            logger.error("Ссылка не прикрепислась так как такой записи нет");
            return null;
        }

        appointment.setLink(appointmentPsyDto.getLink());
        appointmentRepository.save(appointment);

        logger.error("Ссылка успешно дабавлена к записи с id = " + appointmentPsyDto.getId());

        return appointment;
    }

    @Transactional
    public Appointment saveNewAppointment(AppointmentCreateDto createDto) throws Exception {
        ZonedDateTime zonedDate = createDto.getDate().toInstant()
                .atZone(ZoneId.systemDefault());
        Psychologist psychologist = psychologistService.findById(createDto.getPsychologistId());
        List<AppointmentDay> appointmentDays = psychologist.getAppointmentDays()
                .stream()
                .filter(e -> e.getDayOfWeek() == zonedDate.toLocalDate().getDayOfWeek()).toList();
        if(appointmentDays.size() == 0 || createDto.getDate().getTime() < new Date().getTime())
        {
            logger.error("В эту дату нельзя записаться");
            throw new RuntimeException("В эту дату нельзя записаться");
        }
        List<LocalTime> localTimes = appointmentDays.get(0).getDates()
                .stream()
                .filter(e -> e.compareTo(zonedDate.toLocalTime()) == 0)
                .toList();
        if (localTimes.size() == 0)
        {
            logger.error("В эту дату нельзя записаться");
            throw new RuntimeException("В эту дату нельзя записаться");
        }
        long appointmentsInThisDate = psychologist.getAppointments()
                .stream()
                .filter(e -> e.getDate().getTime() == createDto.getDate().getTime())
                .count();
        if (appointmentsInThisDate > 0)
        {
            logger.error("В эту дату нельзя записаться");
            throw new RuntimeException("В эту дату нельзя записаться");
        }


        Appointment appointment = new Appointment();
        appointment.setPsychologist(psychologist);
        appointment.setPatient(userService.getCurrentUser().getPatient());
        appointment.setDate(createDto.getDate());
        appointment.setStatus(true);

        Appointment resAppointment = appointmentRepository.save(appointment);
        logger.info("Запись с id = " + resAppointment.getId() + " успешно создана!");

        return resAppointment;
    }

    public Appointment changeStatus(int id) throws Exception
    {
        Appointment appointment = findById(id);
        if (appointment == null)
        {
            logger.error("Ссылка не прикрепислась так как такой записи нет");
            return null;
        }

        appointment.setStatus(!appointment.isStatus());
        Appointment resAppointment = appointmentRepository.save(appointment);

        logger.info("Статус успешно изменен на " + appointment.isStatus()+ " у записи с id = " + appointment.getId());

        return resAppointment;
    }

    public boolean deleteAppointment(int id) {
        Appointment appointment = findById(id);
        if (appointment == null)
        {
            logger.error("Ссылка не прикрепислась так как такой записи нет");
            return false;
        }

        appointmentRepository.delete(appointment);
        logger.info("Запись с id = " + id + " успешно удалена!");

        return true;
    }

    @Transactional
    public List<AppointmentPatientDto> patientActiveAppointments() {
        return userService.getCurrentUser().getPatient().getAppointments()
                .stream()
                .filter(e -> e.isStatus())
                .map(e -> new AppointmentPatientDto(psychologistService.psyToPsyDto(e.getPsychologist()), e.getDate(), e.getLink()))
                .toList();
    }

    @Transactional
    public List<AppointmentPatientDto> patientClosedAppointments()
    {
        return userService.getCurrentUser().getPatient().getAppointments()
                .stream()
                .filter(e -> !e.isStatus())
                .map(e -> new AppointmentPatientDto(psychologistService.psyToPsyDto(e.getPsychologist()), e.getDate(), e.getLink()))
                .toList();
    }

    @Transactional
    public List<AppointmentPsyDto> psyActiveAppointments() {
        return userService.getCurrentUser().getPsychologist().getAppointments()
                .stream()
                .filter(e -> e.isStatus())
                .map(e -> new AppointmentPsyDto(e.getId(), e.getPatient().getName(), e.getDate(), e.getLink()))
                .toList();
    }

    public Appointment close(CloseAppointmentDTO closeAppointmentDTO)
    {
        Appointment appointment = findById(closeAppointmentDTO.getAppointmentId());
        if (appointment.isStatus())
        {
            logger.error("Запись еще не завершена!");
            throw new RuntimeException("Запись еще не завершена!");
        }
        if (appointment.getReview() != null)
        {
            logger.error("На эту запись уже существует отзыв");
            throw new RuntimeException("На эту запись уже существует отзыв");
        }

        Review review = new Review();
        review.setPatient(appointment.getPatient());
        review.setPsychologist(appointment.getPsychologist());
        review.setLiked(closeAppointmentDTO.isLiked());
        review.setText(closeAppointmentDTO.getReviewText());
        review.setStatus(false);

        appointment.setReview(reviewRepository.save(review));

        Appointment resAppointment = appointmentRepository.save(appointment);
        logger.info("Запись с id = " + resAppointment.getId() + " успешно закрыта!");
        return resAppointment;
    }

    private Appointment findById(int id)
    {
        Appointment appointment = null;
        try{
            appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not Found By id = " + id));
            logger.info("Запись с id = " + id + " найдена");
        } catch (RuntimeException ex)
        {
            logger.error(ex.getMessage());
        }
        return appointment;
    }
}
