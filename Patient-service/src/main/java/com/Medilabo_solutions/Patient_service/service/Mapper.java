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
        patient.setFirstName(request.firstName);
        patient.setLastName(request.lastName);
        patient.setDateOfBirth(request.dateOfBirth);

        if (request.gender != null) {
            patient.setGender(Patient.Gender.valueOf(request.gender));
        }

        patient.setAddress(request.address);
        patient.setPhoneNumber(request.phoneNumber);

        return patient;
    }

    /**
     * Converts a Patient entity to a PatientResponseDTO.
     * @param patient the Patient entity to convert
     * @return a PatientResponseDTO containing the data from the patient entity
     */
    public static PatientResponseDTO toResponse(Patient patient) {
        PatientResponseDTO response = new PatientResponseDTO();
        response.id = patient.getId();
        response.firstName = patient.getFirstName();
        response.lastName = patient.getLastName();
        response.dateOfBirth = patient.getDateOfBirth();
        response.gender = patient.getGender() != null ? patient.getGender().name() : null;
        response.address = patient.getAddress();
        response.phoneNumber = patient.getPhoneNumber();

        return response;
    }
}
