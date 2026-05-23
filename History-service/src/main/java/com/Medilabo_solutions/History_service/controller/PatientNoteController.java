package com.Medilabo_solutions.History_service.controller;

import com.Medilabo_solutions.History_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.History_service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.History_service.model.PatientNote;
import com.Medilabo_solutions.History_service.service.PatientNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Slf4j
public class PatientNoteController {
    private final PatientNoteService patientNoteService;

    @PostMapping("/patient/id/{patientId}")
    public ResponseEntity<PatientNoteResponseDTO> addNote(@PathVariable Long patientId, @RequestBody @Valid PatientNoteRequestDTO note) {
        log.info("Received request to add note for patient ID: {}", patientId);
        return ResponseEntity.ok(patientNoteService.addNote(patientId, note));
    }

    @GetMapping("/patient/id/{patientId}")
    public ResponseEntity<List<PatientNoteResponseDTO>> getHistory(@PathVariable Long patientId) {
        log.info("Received request to get history for patient ID: {}", patientId);
        return ResponseEntity.ok(patientNoteService.getHistory(patientId));
    }
}
