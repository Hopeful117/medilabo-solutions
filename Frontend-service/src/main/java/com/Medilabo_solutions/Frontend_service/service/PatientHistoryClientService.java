package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientNoteResponseDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface PatientHistoryClientService {
List<PatientNoteResponseDTO> getPatientNotesByPatientId(Long patientId, HttpSession session);
PatientNoteResponseDTO addPatientNoteToPatient(Long patientId, PatientNoteRequestDTO patientNoteRequest, HttpSession session);
}
