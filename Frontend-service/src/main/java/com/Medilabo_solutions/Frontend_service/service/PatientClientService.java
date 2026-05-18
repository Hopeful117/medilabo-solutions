package com.Medilabo_solutions.Frontend_service.service;

import com.Medilabo_solutions.Frontend_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Frontend_service.dto.PatientResponseDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface PatientClientService {
    List<PatientResponseDTO> getAllPatients(HttpSession session);
    PatientResponseDTO getPatientById(Long id, HttpSession session);
    List<PatientResponseDTO> getPatientsByLastName(String name, HttpSession session);
    PatientResponseDTO getPatientByLastNameAndFirstName(String lastName, String firstName, HttpSession session);
     PatientResponseDTO createPatient(PatientRequestDTO patientRequest, HttpSession session);
     PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequest, HttpSession session);
     void deletePatientById(Long id, HttpSession session);
}
