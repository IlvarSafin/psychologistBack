package com.example.psyBack.controller;

import com.example.psyBack.dto.PsychologistChangesDTO;
import com.example.psyBack.dto.PsychologistDTO;
import com.example.psyBack.dto.ReviewDTO;
import com.example.psyBack.dto.SignUpPsychologistRequest;
import com.example.psyBack.entity.Psychologist;
import com.example.psyBack.entity.PsychologistChanges;
import com.example.psyBack.entity.Review;
import com.example.psyBack.service.PsychologistChangesService;
import com.example.psyBack.service.PsychologistService;
import com.example.psyBack.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PsychologistService psychologistService;
    private final ReviewService reviewService;
    private final PsychologistChangesService psychologistChangesService;

    @GetMapping("/newPsychologists")
    public ResponseEntity<List<PsychologistDTO>> getNewPsys()
    {
        return ResponseEntity.ok(
                psychologistService.getAllNoAccessedPsychologist()
                        .stream()
                        .map(psychologistService::psyToPsyDto)
                        .toList()
        );
    }

    @GetMapping("/psy")
    public ResponseEntity<SignUpPsychologistRequest> getPsy(@Param("psyId") int psyId) throws Exception {
        return ResponseEntity.ok(psychologistService.psyToSignUpReq(psyId));
    }

    @PostMapping("/changeStatusPsy")
    public ResponseEntity<Boolean> changePsyStatus(@Param("psyId") int psyId) throws Exception {
        Psychologist psychologist = psychologistService.changeStatus(psyId);
        return ResponseEntity.ok(psychologist != null);
    }

    @GetMapping("/badReviews")
    public ResponseEntity<List<ReviewDTO>> getBadReviews()
    {
        return ResponseEntity.ok(
                reviewService.getBadNoAcceptedReviews()
                        .stream()
                        .map(e -> new ReviewDTO(e.getId(), e.getText()))
                        .toList()
        );
    }

    @GetMapping("/goodReviews")
    public ResponseEntity<List<ReviewDTO>> getGoodReviews()
    {
        return ResponseEntity.ok(
                reviewService.getGoodNoAcceptedReviews()
                        .stream()
                        .map(e -> new ReviewDTO(e.getId(), e.getText()))
                        .toList()
        );
    }

    @PostMapping("/changeStatusReview")
    public ResponseEntity<Boolean> changeStatusReview(int reviewId)
    {
        return ResponseEntity.ok(reviewService.changeStatus(reviewId) != null);
    }

    @GetMapping("/psyChanges")
    public ResponseEntity<List<PsychologistChangesDTO>> getPsyChanges()
    {
        return ResponseEntity.ok(
                psychologistChangesService.getAll()
                    .stream()
                    .map(e -> new PsychologistChangesDTO(e.getId(), e.getDescription(), psychologistService.psyToPsyDto(e.getPsychologist())))
                    .toList()
        );
    }

    @GetMapping("/psyChange")
    public ResponseEntity<PsychologistChangesDTO> getPsyChange(@Param("pcId") int pcId)
    {
        PsychologistChanges pc = psychologistChangesService.getById(pcId);
        PsychologistChangesDTO dto = new PsychologistChangesDTO(pc.getId(), pc.getDescription(), psychologistService.psyToPsyDto(pc.getPsychologist()));
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/psyChange")
    public ResponseEntity<Boolean> psyChangeAccept(@Param("pcId") int pcId)
    {
        psychologistChangesService.acceptChanges(pcId);
        return ResponseEntity.ok(true);
    }
}
