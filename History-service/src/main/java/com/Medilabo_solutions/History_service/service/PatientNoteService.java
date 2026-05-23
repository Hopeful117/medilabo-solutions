package com.Medilabo_solutions.History_service.service;

import com.Medilabo_solutions.History_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.History_service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.History_service.model.PatientNote;

import java.util.List;

public interface PatientNoteService {
    PatientNoteResponseDTO addNote(Long patientId ,PatientNoteRequestDTO request);
     List<PatientNoteResponseDTO> getHistory(Long patientId);
}
