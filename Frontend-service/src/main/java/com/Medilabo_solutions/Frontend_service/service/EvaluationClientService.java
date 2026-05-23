package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.EvaluationResponseDTO;
import jakarta.servlet.http.HttpSession;

public interface EvaluationClientService {
    EvaluationResponseDTO getEvaluationByPatientId(long patientId, HttpSession session);
}
