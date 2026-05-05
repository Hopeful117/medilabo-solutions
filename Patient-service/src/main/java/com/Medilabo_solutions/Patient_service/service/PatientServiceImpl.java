package com.Medilabo_solutions.Patient_service.service;

import com.Medilabo_solutions.Patient_service.exception.DuplicateResourceException;
import com.Medilabo_solutions.Patient_service.exception.ResourceNotFoundException;
import com.Medilabo_solutions.Patient_service.model.Patient;
import com.Medilabo_solutions.Patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing patients.
 */
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    /**
     * Retrieves all patients from the database.
     *
     * @return a list of all patients
     */
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Retrieves a patient by their ID.
     * @param id the ID of the patient to retrieve
     * @return the patient with the specified ID
      * @throws ResourceNotFoundException if no patient is found with the given ID
     */
    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }
    /**
     * Creates a new patient in the database.
     * @param patient the patient to create
     * @return the created patient
     * @throws DuplicateResourceException if a patient with the same first and last name already exists
     */
    @Override
    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByLastNameAndFirstName(patient.getLastName(), patient.getFirstName())) {
            throw new DuplicateResourceException(
                    "Patient with name " + patient.getFirstName() + " " + patient.getLastName() + " already exists."
            );
        }
        return patientRepository.save(patient);
    }
    /**
     * Updates an existing patient in the database.
     * @param id the ID of the patient to update
     * @param patient the updated patient information
     * @return the updated patient
     * @throws ResourceNotFoundException if no patient is found with the given ID
     */
    @Override
    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = getPatientById(id);

        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setGender(patient.getGender());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setPhoneNumber(patient.getPhoneNumber());

        return patientRepository.save(existingPatient);
    }
    /**
     * Deletes a patient from the database.
     * @param id the ID of the patient to delete
     * @throws ResourceNotFoundException if no patient is found with the given ID
     */
    @Override
    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }

    /**
     * Retrieves a list of patients by their last name.
     * @param lastName the last name to search for
     * @return a list of patients with the specified last name
     */
    @Override
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastName(lastName);
    }
    /**
     * Retrieves a patient by their last name and first name.
     * @param lastName the last name of the patient
     * @param firstName the first name of the patient
     * @return an Optional containing the patient if found, or empty if not found
     */
    @Override
    public Optional <Patient> getPatientByLastNameAndFirstName(String lastName, String firstName) {
        return Optional.ofNullable(patientRepository.findByLastNameAndFirstName(lastName, firstName));
    }
}






