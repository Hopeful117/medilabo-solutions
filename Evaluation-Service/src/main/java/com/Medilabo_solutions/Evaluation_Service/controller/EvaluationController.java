package com.Medilabo_solutions.Evaluation_Service.controller;

import com.Medilabo_solutions.Evaluation_Service.dto.EvaluationResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.service.EvaluationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;

    @GetMapping("/patient/id/{patientId}")
    public ResponseEntity<EvaluationResponseDTO> evaluatePatientRisk(@PathVariable Long patientId) {
        log.info("Received request to evaluate risk for patient ID: {}", patientId);

            EvaluationResponseDTO response = evaluationService.evaluatePatientRisk(patientId);
            return ResponseEntity.ok(response);

    }
}
