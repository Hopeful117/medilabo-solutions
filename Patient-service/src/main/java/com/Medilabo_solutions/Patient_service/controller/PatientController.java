package com.Medilabo_solutions.Patient_service.controller;

import com.Medilabo_solutions.Patient_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Patient_service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Patient_service.model.Patient;
import com.Medilabo_solutions.Patient_service.service.Mapper;
import com.Medilabo_solutions.Patient_service.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller for managing patients.
 * Provides endpoints for CRUD operations on patient records.
 */
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    /**
     * Get all patients.
     *
     * @return List of PatientResponseDTO wrapped in ResponseEntity
     */
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        List<PatientResponseDTO> response= patientService.getAllPatients().stream()
                .map(Mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a patient by ID.
     *
     * @param id Patient ID
     * @return PatientResponseDTO wrapped in ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    /**
     * Create a new patient.
     *
     * @param request PatientRequestDTO containing patient details
     * @return Created PatientResponseDTO wrapped in ResponseEntity with HTTP status 201
     */
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO request) {

    Patient patient = Mapper.toEntity(request);
    Patient created = patientService.createPatient(patient);
    return new ResponseEntity<>(Mapper.toResponse(created), HttpStatus.CREATED);
    }

    /**
     * Update an existing patient.
     *
     * @param id      Patient ID
     * @param request PatientRequestDTO containing updated patient details
     * @return Updated PatientResponseDTO wrapped in ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable long id,
                                                 @Valid @RequestBody PatientRequestDTO request ){
        Patient patient = Mapper.toEntity(request);
        Patient updated = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(Mapper.toResponse(updated));
    }

        /**
        * Delete a patient by ID.
        *
        * @param id Patient ID
        * @return ResponseEntity with HTTP status 204 (No Content)
        */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
