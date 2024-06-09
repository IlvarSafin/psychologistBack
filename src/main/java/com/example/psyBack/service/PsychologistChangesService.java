package com.example.psyBack.service;

import com.example.psyBack.dto.PsychologistChangesDTO;
import com.example.psyBack.dto.PsychologistDTO;
import com.example.psyBack.entity.PsychologistChanges;
import com.example.psyBack.repository.PsychologistChangesRepository;
import com.example.psyBack.repository.PsychologistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PsychologistChangesService {
    private final PsychologistChangesRepository psychologistChangesRepository;
    private final PsychologistRepository psychologistRepository;

    @Transactional
    public List<PsychologistChanges> getAll()
    {
        return psychologistChangesRepository.findAll();
    }

    @Transactional
    public PsychologistChanges getById(int id)
    {
        return psychologistChangesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Изменений нет с id = " + id));
    }

    @Transactional
    public void acceptChanges(int id)
    {
        PsychologistChanges pc = getById(id);
        pc.getPsychologist().setDescription(pc.getDescription());
        psychologistRepository.save(pc.getPsychologist());
        psychologistChangesRepository.delete(pc);
    }
}
