package com.example.psyBack.service;

import com.example.psyBack.dto.*;
import com.example.psyBack.entity.*;
import com.example.psyBack.repository.AppointmentRepository;
import com.example.psyBack.repository.PatientRepository;
import com.example.psyBack.repository.PsychologistChangesRepository;
import com.example.psyBack.repository.PsychologistRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PsychologistService {
    private static final Logger logger = LoggerFactory.getLogger(PsychologistService.class);
    private PsychologistRepository psychologistRepository;
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;
    private ModelMapper modelMapper;
    private PsychologistChangesRepository psychologistChangesRepository;
    private UserService userService;

    private static final int MAX_SIZE_REVIEWS = 5;

    @Autowired
    public PsychologistService(
            UserService userService,
            PsychologistChangesRepository psychologistChangesRepository,
            AppointmentRepository appointmentRepository,
            PsychologistRepository psychologistRepository,
            PatientRepository patientRepository,
            ModelMapper modelMapper
            )
    {
        this.userService = userService;
        this.psychologistChangesRepository = psychologistChangesRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.psychologistRepository = psychologistRepository;
        this.modelMapper = modelMapper;
    }

    public List<Psychologist> getAllAccessedPsychologist()
    {
        return psychologistRepository.findAll()
                .stream()
                .filter(Psychologist::isStatus)
                .toList();
    }

    public List<Psychologist> getAllNoAccessedPsychologist()
    {
        return psychologistRepository.findAll()
                .stream()
                .filter(e -> !e.isStatus())
                .toList();
    }

    public List<Psychologist> getFilteredPsychologists(PsychologistDTO psychologistDTO)
    {
        List<Psychologist> psychologists = getAllAccessedPsychologist();
        Map<Integer, List<Psychologist>> countFiltersPsy = new HashMap<>();
        List<Psychologist> result = new ArrayList<>();

        for (Psychologist psy : psychologists)
        {
            psy.setReviews(psy.getReviews().stream().filter(e -> e.isLiked() && e.isStatus()).toList());
            if (psychologistDTO.getName() != null && !psychologistDTO.getName().isEmpty() && !psy.getName().contains(psychologistDTO.getName()))
            {
                continue;
            }

            if (psychologistDTO.getSex() != ' ' && psychologistDTO.getSex() != psy.getSex())
            {
                continue;
            }

            int count = (int)psy.getSpecializations().stream().filter(f -> psychologistDTO.getSpecializations().contains(f)).count();
            if(psychologistDTO.getSpecializations().size() != 0 && count == 0)
            {
                continue;
            }

            int countForRightSorting = Specialization.values().length - count;
            if (!countFiltersPsy.containsKey(countForRightSorting)){
                List<Psychologist> list = new ArrayList<>();
                list.add(psy);
                countFiltersPsy.put(countForRightSorting, list);
            }
            else {
                countFiltersPsy.get(countForRightSorting).add(psy);
            }
        }

        for (Map.Entry<Integer, List<Psychologist>> psyList : countFiltersPsy.entrySet())
        {
            result.addAll(psyList.getValue());
        }

        return result;
    }

    public Psychologist getAllInformation(int id) throws Exception {
        Psychologist psychologist = findById(id);

        if (!psychologist.isStatus())
        {
            logger.info("Психолог с id = " + id + " не подтвержден");
            throw new RuntimeException("Psychologist with id = " + id + " not accepted");
        }

        return psychologist;
    }

    public Psychologist changeStatus(int id) throws Exception {
        Psychologist psychologist = findById(id);
        psychologist.setStatus(!psychologist.isStatus());

        Psychologist savedPsy = psychologistRepository.save(psychologist);
        logger.info("Статус у психолога с id = " + id + " успешно изменен");

        return savedPsy;
    }

    public List<Review> getGoodReviewsByPsychologist(int psyId, int countReviews) throws Exception {
        return findById(psyId).getReviews()
                .stream()
                .filter(e -> e.isLiked() && e.isStatus())
                .limit(countReviews + MAX_SIZE_REVIEWS)
                .toList();
    }

    @Transactional
    public boolean editPsy(PsychologistDTO psychologistDTO) {
        Psychologist psychologist = userService.getCurrentUser().getPsychologist();
        if (!psychologistDTO.getDescription().equals(psychologist.getDescription()))
        {

            List<PsychologistChanges> psychologistChangesList = psychologistChangesRepository.findByPsychologist(psychologist);

            PsychologistChanges psychologistChanges = psychologistChangesList.size() > 0 ? psychologistChangesList.get(0) : new PsychologistChanges();
            psychologistChanges.setPsychologist(psychologist);
            psychologistChanges.setDescription(psychologistDTO.getDescription());

            psychologistChangesRepository.save(psychologistChanges);
        }

        if (!psychologist.getSpecializations().containsAll(psychologistDTO.getSpecializations()))
        {
            psychologist.setSpecializations(psychologistDTO.getSpecializations());
            psychologistRepository.save(psychologist);
        }
        logger.info("Изменения у психолога с id = " + psychologistDTO.getId() + " успешно сохранились");

        return true;
    }

    public Psychologist findById(int id) throws Exception {
        return psychologistRepository.findById(id)
                .orElseThrow(() -> new Exception("Not Found By id = " + id));
    }

    public PsychologistDTO psyToPsyDto(Psychologist psy)
    {
        int countReviews = Math.min(psy.getReviews().size(), MAX_SIZE_REVIEWS);
        return new PsychologistDTO(
                psy.getId(),
                psy.getName(),
                psy.getDescription(),
                countReviews,
                psy.getSex(),
                psy.getPhoto(),
                psy.getReviews().size() <= countReviews,
                psy.getSpecializations()
        );
    }

    public PsychologistInfoDTO psyToPsyInfoDto(Psychologist psychologist) {
        List<Date> busyDates = psychologist.getAppointments()
                .stream()
                .map(Appointment::getDate)
                .toList();

        List<AppointmentDayDTO> appointmentDayDTOS = psychologist.getAppointmentDays()
                .stream()
                .map(e -> new AppointmentDayDTO(e.getDayOfWeek(), e.getDates()))
                .toList();

        List<ReviewDTO> reviewDTOS = psychologist.getReviews()
                .stream()
                .filter(e -> e.isLiked() && e.isStatus())
                .map(e -> new ReviewDTO(e.getText()))
                .toList();

        PsychologistInfoDTO resultDto = new PsychologistInfoDTO(psyToPsyDto(psychologist));
        resultDto.setAppointmentDays(appointmentDayDTOS);
        resultDto.setBusyDates(busyDates);
        resultDto.setReviews(reviewDTOS);

        return resultDto;
    }

    @Transactional
    public SignUpPsychologistRequest psyToSignUpReq(int id) throws Exception {
        Psychologist psychologist = findById(id);

        List<AppointmentDayDTO> dayDTOS = psychologist.getAppointmentDays()
                .stream()
                .map(e -> new AppointmentDayDTO(e.getDayOfWeek(), e.getDates()))
                .toList();
        List<String> docs = psychologist.getDocumentPsyList()
                .stream()
                .map(e -> Base64.getEncoder().encodeToString(e.getDocument()))
                .toList();

        SignUpPsychologistRequest dto = new SignUpPsychologistRequest(
                psychologist.getAge(),
                psychologist.getDescription(),
                psychologist.getSex(),
                psychologist.getExperience(),
                psychologist.getPrice(),
                dayDTOS,
                psychologist.getPhoto() != null ? Base64.getEncoder().encodeToString(psychologist.getPhoto()) : null,
                docs
        );
        dto.setName(psychologist.getName());
        return dto;
    }
}
