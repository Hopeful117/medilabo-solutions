package com.Medilabo_solutions.Patient_service.service;

import com.Medilabo_solutions.Patient_service.model.Patient;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing patients.
 */
public interface PatientService {
    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient createPatient(Patient patient);

    Patient updatePatient(Long id, Patient patient);

    void deletePatient(Long id);

    List<Patient> getPatientsByLastName(String lastName);

    Patient getPatientByLastNameAndFirstName(String lastName, String firstName);

}