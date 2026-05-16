package com.Medilabo_solutions.History_service.controller;

import com.Medilabo_solutions.History_service.model.PatientNote;
import com.Medilabo_solutions.History_service.service.PatientNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Slf4j
public class PatientNoteController {
    private final PatientNoteService patientNoteService;

    @PostMapping("/patient/id/{patientId}")
    public PatientNote addNote(@PathVariable Long patientId, @RequestBody String note) {
        log.info("Received request to add note for patient ID: {}", patientId);
        return patientNoteService.addNote(patientId, note);
    }

    @GetMapping("/patient/id/{patientId}")
    public List<PatientNote> getHistory(@PathVariable Long patientId) {
        log.info("Received request to get history for patient ID: {}", patientId);
        return patientNoteService.getHistory(patientId);
    }
}
