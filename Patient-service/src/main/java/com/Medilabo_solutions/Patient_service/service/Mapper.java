package com.Medilabo_solutions.Patient_service.service;

import com.Medilabo_solutions.Patient_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Patient_service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Patient_service.model.Patient;

public class Mapper {
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
