package com.Medilabo_solutions.History_service.repository;

import com.Medilabo_solutions.History_service.model.PatientNote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PatientNoteRepository extends MongoRepository<PatientNote, String> {
    List<PatientNote> findByPatientId(Long patientId);
}
