package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.helper.GetAuthHeaders;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientHistoryClientServiceImpl implements PatientHistoryClientService {
    private final RestTemplate restTemplate;
    private static final String PATIENT_HISTORY_SERVICE_URL = "http://gateway:8080/api/v1/history";


    @Override
    public List<PatientNoteResponseDTO> getPatientNotesByPatientId(Long patientId,String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            PatientNoteResponseDTO[] notes = restTemplate.exchange(
                    PATIENT_HISTORY_SERVICE_URL + "/patient/id/" + patientId,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    PatientNoteResponseDTO[].class
            ).getBody();
            return List.of(notes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve patient notes for patient ID: " + patientId, e);
        }
    }
}
