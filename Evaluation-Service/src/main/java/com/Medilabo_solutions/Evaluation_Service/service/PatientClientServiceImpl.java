package com.Medilabo_solutions.Evaluation_Service.service;


import com.Medilabo_solutions.Evaluation_Service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.helper.GetAuthHeaders;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PatientClientServiceImpl implements PatientClientService {
    private final RestTemplate restTemplate;
    private static final String PATIENT_SERVICE_URL = "http://localhost:8080/api/v1/patients";


    @Override
    public PatientResponseDTO getPatientById(Long patientId, String token ){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(
                    PATIENT_SERVICE_URL + "/id/" + patientId,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    PatientResponseDTO.class
            ).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve patient with ID: " + patientId, e);
        }
    }



}
