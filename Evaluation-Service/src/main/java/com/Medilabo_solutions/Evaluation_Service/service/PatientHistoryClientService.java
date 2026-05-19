package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.PatientNoteResponseDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface PatientHistoryClientService {
    List<PatientNoteResponseDTO> getPatientNotesByPatientId(Long patientId, String token);

}
