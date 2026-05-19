package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.EvaluationResponseDTO;
import jakarta.servlet.http.HttpSession;

public interface EvaluationService {
EvaluationResponseDTO evaluatePatientRisk (Long patientId, String token);
}
