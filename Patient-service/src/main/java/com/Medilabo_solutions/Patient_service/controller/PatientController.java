package com.Medilabo_solutions.Patient_service.controller;

import com.Medilabo_solutions.Patient_service.dto.PatientRequestDTO;
import com.Medilabo_solutions.Patient_service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Patient_service.model.Patient;
import com.Medilabo_solutions.Patient_service.service.Mapper;
import com.Medilabo_solutions.Patient_service.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PatientController {
    private final PatientService patientService;

    /**
     * Get all patients.
     *
     * @return List of PatientResponseDTO wrapped in ResponseEntity
     */
    @GetMapping("/all")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        log.info("Received request to get all patients");
        List<PatientResponseDTO> response = patientService.getAllPatients().stream()
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
    @GetMapping("/id/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        log.info("Received request to get patient with ID: {}", id);
        PatientResponseDTO responseDTO = Mapper.toResponse(patientService.getPatientById(id));
        return ResponseEntity.ok(Mapper.toResponse(patientService.getPatientById(id)));
    }

    /**
     * Get patients by last name.
     *
     * @param lastName Patient's last name
     * @return List of PatientResponseDTO wrapped in ResponseEntity
     */
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<PatientResponseDTO>> getPatientsByLastName(@PathVariable String lastName) {
        log.info("Received request to get patients with last name: {}", lastName);
        List<PatientResponseDTO> response = patientService.getPatientsByLastName(lastName).stream()
                .map(Mapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a patient by last name and first name.
     *
     * @param lastName  Patient's last name
     * @param firstName Patient's first name
     * @return PatientResponseDTO wrapped in ResponseEntity
     */
    @GetMapping("/{lastName}/{firstName}")
    public ResponseEntity<PatientResponseDTO> getPatientByLastNameAndFirstName(@PathVariable String lastName, @PathVariable String firstName) {
        log.info("Received request to get patient with last name: {} and first name: {}", lastName, firstName);
        PatientResponseDTO response = Mapper.toResponse(patientService.getPatientByLastNameAndFirstName(lastName, firstName));
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new patient.
     *
     * @param request PatientRequestDTO containing patient details
     * @return Created PatientResponseDTO wrapped in ResponseEntity with HTTP status 201
     */
    @PostMapping("/add")
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO request) {
        log.info("Received request to create patient: {} {}", request.getFirstName(), request.getLastName());
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
    @PutMapping("/update/id/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable long id,
                                                            @Valid @RequestBody PatientRequestDTO request) {
        log.info("Received request to update patient with ID: {}", id);
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
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.info("Received request to delete patient with ID: {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

}
