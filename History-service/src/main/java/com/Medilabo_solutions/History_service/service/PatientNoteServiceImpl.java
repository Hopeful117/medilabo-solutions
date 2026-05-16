package com.Medilabo_solutions.History_service.service;

import com.Medilabo_solutions.History_service.model.PatientNote;
import com.Medilabo_solutions.History_service.repository.PatientNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class PatientNoteServiceImpl implements PatientNoteService {
    private final PatientNoteRepository patientNoteRepository;

    @Override
    public PatientNote addNote(Long patientId, String note) {
        log.info("Adding note for patient ID: {}", patientId);
        PatientNote patientNote = PatientNote.builder()
                .patientId(patientId)
                .note(note)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        log.info("Successfully created note for patient ID: {}", patientId);
        return patientNoteRepository.save(patientNote);


    }

    @Override
    public List<PatientNote> getHistory(Long patientId) {
        log.info("Retrieving history for patient ID: {}", patientId);
        return patientNoteRepository.findByPatientId(patientId);
    }
}
