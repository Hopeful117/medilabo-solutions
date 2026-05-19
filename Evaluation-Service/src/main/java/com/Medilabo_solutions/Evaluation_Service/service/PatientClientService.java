package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.PatientResponseDTO;
import jakarta.servlet.http.HttpSession;

public interface PatientClientService {
    PatientResponseDTO getPatientById(Long patientId, String Token);
}
