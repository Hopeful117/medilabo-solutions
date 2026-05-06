package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientClientService {
    private final RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8080/api/v1/patients/";

    public List<PatientResponseDTO> getAllPatients() {
        ResponseEntity<PatientResponseDTO[]> response =
                restTemplate.getForEntity(BASE_URL, PatientResponseDTO[].class);

        return Arrays.asList(response.getBody());
    }

    public PatientResponseDTO getPatientById(Long id) {
        ResponseEntity<PatientResponseDTO> response =
                restTemplate.getForEntity(BASE_URL + "/id/" + id, PatientResponseDTO.class);

        return response.getBody();
    }

    public List<PatientResponseDTO> getPatientsByLastName(String lastName) {
        ResponseEntity<PatientResponseDTO[]> response =
                restTemplate.getForEntity(BASE_URL + "/lastname/" + lastName, PatientResponseDTO[].class);

        return Arrays.asList(response.getBody());
    }

    public PatientResponseDTO getPatientByLastNameandFirstName(String lastName, String firstName) {
        ResponseEntity<PatientResponseDTO> response =
                restTemplate.getForEntity(BASE_URL + "/lastname/" + lastName + "/firstname/" + firstName, PatientResponseDTO.class);

        return response.getBody();
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patient) {
        ResponseEntity<PatientResponseDTO> response =
                restTemplate.postForEntity(BASE_URL, patient, PatientResponseDTO.class);

        return response.getBody();
    }
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patient) {
        restTemplate.put(BASE_URL + "/id/" + id, patient);
        return getPatientById(id);
    }

    public void deletePatientById(Long id) {
        restTemplate.delete(BASE_URL + "/id/" + id);
    }
}
