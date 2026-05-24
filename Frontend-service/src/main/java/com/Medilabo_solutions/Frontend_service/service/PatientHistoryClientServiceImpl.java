package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.Frontend_service.helper.GetAuthHeaders;
import com.Medilabo_solutions.Frontend_service.helper.RestTemplateHelper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientHistoryClientServiceImpl implements PatientHistoryClientService {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://gateway:8080/api/v1/history";
    private final GetAuthHeaders getAuthHeaders;

    @Override
    public List<PatientNoteResponseDTO> getPatientNotesByPatientId(Long patientId, HttpSession session) {
        HttpHeaders headers = getAuthHeaders.getAuthHeaders(session);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info("Calling History API to get history for patient ID: {}", patientId);
        try {
            ResponseEntity<PatientNoteResponseDTO[]> response =
                    restTemplate.exchange(BASE_URL + "/patient/id/" + patientId, HttpMethod.GET, entity, PatientNoteResponseDTO[].class);
            log.info("Successfully retrieved history for patient ID: {}", patientId);
            return Arrays.asList(response.getBody());
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }
    @Override
    public PatientNoteResponseDTO addPatientNoteToPatient(Long patientId, PatientNoteRequestDTO patientNoteRequest, HttpSession session) {
        HttpHeaders headers = getAuthHeaders.getAuthHeaders(session);
        HttpEntity<PatientNoteRequestDTO> entity = new HttpEntity<>(patientNoteRequest, headers);
        log.info("Calling History API to add note for patient ID: {}", patientId);
        try {
            ResponseEntity<PatientNoteResponseDTO> response =
                    restTemplate.exchange(BASE_URL + "/patient/id/" + patientId, HttpMethod.POST, entity, PatientNoteResponseDTO.class);
            log.info("Successfully added note for patient ID: {}", patientId);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }




}
