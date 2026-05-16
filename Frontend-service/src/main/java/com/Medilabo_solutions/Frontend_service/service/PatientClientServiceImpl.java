package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
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
/**
 * Service responsible for communicating with the Patient API to perform CRUD operations on patient data.
 * It sends requests to the Patient API and processes the responses, handling any exceptions that may occur.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PatientClientService {
    private final RestTemplate restTemplate;


    private final String BASE_URL = "http://localhost:8080/api/v1/patients";

    public List<PatientResponseDTO> getAllPatients(HttpSession session) {
        HttpHeaders headers = getAuthHeaders(session);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        log.info("Calling Patient API to get all patients");

        try {
            ResponseEntity<PatientResponseDTO[]> response =
                    restTemplate.exchange(
                            BASE_URL + "/all",
                            HttpMethod.GET,
                            entity,
                            PatientResponseDTO[].class
                    );
            log.info("Successfully retrieved patients");
            return Arrays.asList(response.getBody());
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }

    public PatientResponseDTO getPatientById(Long id,HttpSession session) {
        HttpHeaders headers = getAuthHeaders(session);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        log.info("Calling Patient API to get patient with ID: {}", id);
        try {
            ResponseEntity<PatientResponseDTO> response =
                    restTemplate.exchange(BASE_URL + "/id/" + id, HttpMethod.GET, entity, PatientResponseDTO.class);
        log.info("Successfully retrieved patient with ID: {}", id);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }

    public List<PatientResponseDTO> getPatientsByLastName(String lastName,HttpSession session) {
        HttpHeaders headers = getAuthHeaders(session);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        log.info("Calling Patient API to get patients with last name: {}", lastName);
        try {
            ResponseEntity<PatientResponseDTO[]> response =
                    restTemplate.exchange(BASE_URL + "/lastname/" + lastName, HttpMethod.GET, entity, PatientResponseDTO[].class);
        log.info("Successfully retrieved patients with last name: {}", lastName);
            return Arrays.asList(response.getBody());
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }

    public PatientResponseDTO getPatientByLastNameAndFirstName(String lastName, String firstName,HttpSession session) {
        HttpHeaders headers = getAuthHeaders(session);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        log.info("Calling Patient API to get patient with last name: {} and first name: {}", lastName, firstName);
        try {
            ResponseEntity<PatientResponseDTO> response =
                    restTemplate.exchange(BASE_URL + "/lastname/" + lastName + "/firstname/" + firstName, HttpMethod.GET, entity, PatientResponseDTO.class);
        log.info("Successfully retrieved patient with last name: {} and first name: {}", lastName, firstName);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patient,HttpSession session) {
        HttpHeaders headers = getAuthHeaders(session);
        HttpEntity<PatientRequestDTO> entity = new HttpEntity<>(patient, headers);
        log.info("Calling Patient API to create patient with name: {} {}", patient.getFirstName(), patient.getLastName());
        try {
            ResponseEntity<PatientResponseDTO> response =
                    restTemplate.exchange(BASE_URL + "/add", HttpMethod.POST, entity, PatientResponseDTO.class);
        log.info("Successfully created patient with name: {} {}", patient.getFirstName(), patient.getLastName());
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patient,HttpSession session) {
        HttpHeaders headers = getAuthHeaders(session);
        log.info("Calling Patient API to update patient with ID: {}", id);
        try {
        HttpEntity<PatientRequestDTO> entity = new HttpEntity<>(patient, headers);
        restTemplate.exchange(BASE_URL + "/update/id/" + id, HttpMethod.PUT, entity, Void.class);
        log.info("Successfully updated patient with ID: {}", id);
        return getPatientById(id,session);
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }

    public void deletePatientById(Long id,HttpSession session) {
        HttpHeaders headers = getAuthHeaders(session);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        log.info("Calling Patient API to delete patient with ID: {}", id);
        try {
        restTemplate.exchange(BASE_URL + "/delete/id/" + id, HttpMethod.DELETE, entity, Void.class);
        log.info("Successfully deleted patient with ID: {}", id);
        } catch (HttpStatusCodeException ex) {
            throw RestTemplateHelper.handleException(ex);
        }
    }

    private HttpHeaders getAuthHeaders(HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
