package com.Medilabo_solutions.Evaluation_Service.controller;

import com.Medilabo_solutions.Evaluation_Service.dto.EvaluationResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.exception.PatientNotFoundException;
import com.Medilabo_solutions.Evaluation_Service.helper.RiskLevels;
import com.Medilabo_solutions.Evaluation_Service.service.EvaluationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EvaluationController.class)
public class EvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EvaluationService evaluationService;

    /**
     * Test cases:
     * GET /evaluate/patient/{id} -> 200 + evaluation result
     * GET /evaluate/patient/{id} -> 404 if patient not found
     */


    @Test
    @DisplayName("GET /evaluation/patient/id/{patientId} - Success")
    void testEvaluatePatient() throws Exception {
        Long patientId = 1L;
        String token = "Bearer some-valid-jwt-token";
        EvaluationResponseDTO mockResponse = new EvaluationResponseDTO();
        mockResponse.setRiskLevel(RiskLevels.NONE);
        mockResponse.setId(patientId);

        when(evaluationService.evaluatePatientRisk(patientId)).thenReturn(mockResponse);
        mockMvc.perform(get("/evaluation/patient/id/{patientId}", patientId)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientId))
                .andExpect(jsonPath("$.riskLevel").value("NONE"));

    }

    @Test
    @DisplayName("GET /evaluation/patient/id/{patientId} - Patient Not Found")
    void testEvaluatePatient_NotFound() throws Exception {
        Long patientId = 999L;
        String token = "Bearer some-valid-jwt-token";

        when(evaluationService.evaluatePatientRisk(patientId)).thenThrow(new PatientNotFoundException("Patient not found"));
        mockMvc.perform(get("/evaluation/patient/id/{patientId}", patientId)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

}