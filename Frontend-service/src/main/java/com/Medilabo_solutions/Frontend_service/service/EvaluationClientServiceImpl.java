package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.EvaluationResponseDTO;
import com.Medilabo_solutions.Frontend_service.helper.GetAuthHeaders;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class EvaluationClientServiceImpl implements EvaluationClientService {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://gateway:8080/api/v1/evaluation";
    private final GetAuthHeaders getAuthHeaders;

    @Override
  public EvaluationResponseDTO getEvaluationByPatientId(long patientId, HttpSession session) {
        log.info("Calling Evaluation API to get evaluation for patient ID: {}", patientId);
        try {
            HttpHeaders headers = getAuthHeaders.getAuthHeaders(session);
            HttpEntity<Void>Entity = new HttpEntity<>(headers);
            EvaluationResponseDTO response = restTemplate.exchange(
                    BASE_URL + "/patient/id/" + patientId,
                    org.springframework.http.HttpMethod.GET,
                    Entity,
                    EvaluationResponseDTO.class
            ).getBody();
            log.info("Successfully retrieved evaluation for patient ID: {}", patientId);
            return response;
        } catch (Exception ex) {
            log.error("Error while calling Evaluation API for patient ID: {}: {}", patientId, ex.getMessage());
            throw new RuntimeException("Failed to retrieve evaluation for patient ID: " + patientId, ex);
        }
    }
}
