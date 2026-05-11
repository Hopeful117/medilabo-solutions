package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientClientService {
    private final RestTemplate restTemplate;


    private final String BASE_URL = "http://localhost:8080/api/v1/patients";

    public List<PatientResponseDTO> getAllPatients(HttpSession session) {
        String token = session.getAttribute("jwt").toString();


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);


        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PatientResponseDTO[]> response =
                restTemplate.exchange(
                        BASE_URL + "/all",
                        HttpMethod.GET,
                        entity,
                        PatientResponseDTO[].class
                );

        return Arrays.asList(response.getBody());
    }

    public PatientResponseDTO getPatientById(Long id,HttpSession session) {
        String token = session.getAttribute("jwt").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<PatientResponseDTO> response =
                restTemplate.exchange(BASE_URL + "/id/" + id,HttpMethod.GET,entity,PatientResponseDTO.class);

        return response.getBody();
    }

    public List<PatientResponseDTO> getPatientsByLastName(String lastName,HttpSession session) {
        String token = session.getAttribute("jwt").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<PatientResponseDTO[]> response =
                restTemplate.getForEntity(BASE_URL + "/lastname/" + lastName, PatientResponseDTO[].class,entity);

        return Arrays.asList(response.getBody());
    }

    public PatientResponseDTO getPatientByLastNameAndFirstName(String lastName, String firstName,HttpSession session) {
        String token = session.getAttribute("jwt").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<PatientResponseDTO> response =
                restTemplate.getForEntity(BASE_URL + "/lastname/" + lastName + "/firstname/" + firstName, PatientResponseDTO.class,entity);

        return response.getBody();
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patient,HttpSession session) {
        String token = session.getAttribute("jwt").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<PatientResponseDTO> response =
                restTemplate.postForEntity(BASE_URL +"/add", patient, PatientResponseDTO.class,entity);

        return response.getBody();
    }
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patient,HttpSession session) {
        String token = session.getAttribute("jwt").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.put(BASE_URL + "/update/id/" + id, patient,entity);
        return getPatientById(id,session);
    }

    public void deletePatientById(Long id,HttpSession session) {
        String token = session.getAttribute("jwt").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.delete(BASE_URL + "delete/id/" + id,entity);
    }
}
