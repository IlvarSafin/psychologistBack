package com.example.psyBack.dto;

import com.example.psyBack.entity.AppointmentDay;
import com.example.psyBack.entity.Specialization;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PsychologistInfoDTO extends PsychologistDTO{
    private List<ReviewDTO> reviews;
    private List<Date> busyDates;
    private List<AppointmentDayDTO> appointmentDays;

    public PsychologistInfoDTO(PsychologistDTO psychologistDTO)
    {
        super(
                psychologistDTO.getId(),
                psychologistDTO.getName(),
                psychologistDTO.getDescription(),
                psychologistDTO.getCountReview(),
                psychologistDTO.getSex(),
                psychologistDTO.getPhoto(),
                psychologistDTO.isAllReviews(),
                psychologistDTO.getSpecializations()
        );
    }
}
