package com.Medilabo_solutions.Patient_service.service;

import com.Medilabo_solutions.Patient_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Patient_service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Patient_service.model.Patient;

/**
 * Utility class for mapping between Patient entities and DTOs.
 */
public class Mapper {

    /**
     * Converts a PatientRequestDTO to a Patient entity.
     *
     * @param request the PatientRequestDTO containing patient data
     * @return a Patient entity with the data from the request
     */
    public static Patient toEntity(PatientRequestDTO request) {
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());

        if (request.getGender() != null) {
            patient.setGender(Patient.Gender.valueOf(request.getGender()));
        }

        patient.setAddress(request.getAddress());
        patient.setPhoneNumber(request.getPhoneNumber());

        return patient;
    }

    /**
     * Converts a Patient entity to a PatientResponseDTO.
     * @param patient the Patient entity to convert
     * @return a PatientResponseDTO containing the data from the patient entity
     */
    public static PatientResponseDTO toResponse(Patient patient) {
        PatientResponseDTO response = new PatientResponseDTO();
        response.setId(patient.getId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender() != null ? patient.getGender().name() : null);
        response.setAddress(patient.getAddress());
        response.setPhoneNumber(patient.getPhoneNumber());

        return response;
    }
}
