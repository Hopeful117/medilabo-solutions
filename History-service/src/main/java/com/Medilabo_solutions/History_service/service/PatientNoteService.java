package com.Medilabo_solutions.History_service.service;

import com.Medilabo_solutions.History_service.model.PatientNote;

import java.util.List;

public interface PatientNoteService {
    PatientNote addNote(Long patientId, String note);
     List<PatientNote> getHistory(Long patientId);
}
