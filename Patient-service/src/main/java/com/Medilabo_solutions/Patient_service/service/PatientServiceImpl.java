package com.Medilabo_solutions.Patient_service.service;

import com.Medilabo_solutions.Patient_service.exception.DuplicateResourceException;
import com.Medilabo_solutions.Patient_service.exception.ResourceNotFoundException;
import com.Medilabo_solutions.Patient_service.model.Patient;
import com.Medilabo_solutions.Patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByLastNameAndFirstName(patient.getLastName(), patient.getFirstName())) {
            throw new DuplicateResourceException(
                    "Patient with name " + patient.getFirstName() + " " + patient.getLastName() + " already exists."
            );
        }
        return patientRepository.save(patient);
    }

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

    @Override
    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }

    @Override
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastName(lastName);
    }

    @Override
    public Optional <Patient> getPatientByLastNameAndFirstName(String lastName, String firstName) {
        return Optional.ofNullable(patientRepository.findByLastNameAndFirstName(lastName, firstName));
    }
}






