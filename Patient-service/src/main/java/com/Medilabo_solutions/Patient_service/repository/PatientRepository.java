package com.Medilabo_solutions.Patient_service.repository;

import com.Medilabo_solutions.Patient_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Patient entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient>findByLastName(String lastName);
    Patient findByLastNameAndFirstName(String lastName, String firstName);
    boolean existsByLastNameAndFirstName(String lastName, String firstName);



}
