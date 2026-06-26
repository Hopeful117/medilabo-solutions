package com.Medilabo_solutions.History_service.service;

import com.Medilabo_solutions.History_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.History_service.dto.PatientNoteResponseDTO;
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
    public PatientNoteResponseDTO addNote(Long patientId, PatientNoteRequestDTO request) {

        log.info("Adding note for patient ID: {}", patientId);
        PatientNote patientNote = PatientNote.builder()
                .patientId(patientId)
                .note(request.getContent())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        log.info("Successfully created note for patient ID: {}", patientId);
        patientNote = patientNoteRepository.save(patientNote);
        return mapToDTO(patientNote);


    }

    @Override
    public List<PatientNoteResponseDTO> getHistory(Long patientId) {
        log.info("Retrieving history for patient ID: {}", patientId);
        List<PatientNote> notes = patientNoteRepository.findByPatientId(patientId);
        return notes.stream()
                .map(this::mapToDTO)
                .toList();
    }


    private PatientNoteResponseDTO mapToDTO(PatientNote note) {
        return PatientNoteResponseDTO.builder()
                .id(note.getId())
                .patientId(note.getPatientId())
                .note(note.getNote())
                .createdAt(note.getCreatedAt())
                .build();
    }
}