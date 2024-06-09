package com.example.psyBack.controller;

import com.example.psyBack.dto.*;
import com.example.psyBack.entity.Psychologist;
import com.example.psyBack.service.PatientService;
import com.example.psyBack.service.PsychologistService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/client/psychologist")
public class PsychologistClientController {
    private ModelMapper modelMapper;
    private PsychologistService psychologistService;
    private PatientService patientService;

    public static final int PSY_COUNT_IN_PAGE = 2;

    @Autowired
    public PsychologistClientController(
            ModelMapper modelMapper,
            PsychologistService psychologistService,
            PatientService patientService
    )
    {
        this.modelMapper = modelMapper;
        this.patientService = patientService;
        this.psychologistService = psychologistService;
    }

    @GetMapping("/list")
    public ResponseEntity<PsyListRequest> getAllByFilter(
            @Param("pageCount") int pageCount,
            @RequestBody PsychologistDTO dto
    )
    {
        List<Psychologist> psychologists = psychologistService.getFilteredPsychologists(dto);
        PsyListRequest psyListRequest = new PsyListRequest(
                psychologists
                        .stream().map(e -> psychologistService.psyToPsyDto(e))
                        .skip((pageCount - 1) * PSY_COUNT_IN_PAGE)
                        .limit(PSY_COUNT_IN_PAGE)
                        .collect(Collectors.toList()),
                Math.max(psychologists.size() / PSY_COUNT_IN_PAGE, 1)
        );
        return ResponseEntity.ok(psyListRequest);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@Param("psyId") int psyId,
                                                      @Param("countReviews") int countReviews)
    {
        try {
            return new ResponseEntity<>(psychologistService.getGoodReviewsByPsychologist(psyId, countReviews)
                    .stream()
                    .map(e -> new ReviewDTO(e.getText()))
                    .toList(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    @GetMapping("/info")
    public ResponseEntity<PsychologistInfoDTO> getPsyInfo(@Param("psyId") int psyId)
    {
        try {
            Psychologist psychologist = psychologistService.getAllInformation(psyId);
            return new ResponseEntity<>(psychologistService.psyToPsyInfoDto(psychologist), HttpStatus.OK);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
