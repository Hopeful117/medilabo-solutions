package com.Medilabo_solutions.History_service.service;

import com.Medilabo_solutions.History_service.dto.PatientNoteRequestDTO;
import com.Medilabo_solutions.History_service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.History_service.model.PatientNote;
import com.Medilabo_solutions.History_service.repository.PatientNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientNoteServiceTest {
    @Mock
    private PatientNoteRepository patientNoteRepository;

    @InjectMocks
    private PatientNoteServiceImpl patientNoteService;

    @BeforeEach
    void setup() {
        // Mockito will inject mocks
    }
    @Nested
    @DisplayName("Successful operations")
    class SuccessfulOperations {

        @Test
        void addNote_shouldSaveAndReturnDTO() {
            Long patientId = 123L;
            PatientNoteRequestDTO request = new PatientNoteRequestDTO();
            request.setContent("Test note content");

            PatientNote saved = PatientNote.builder()
                    .id("abc123")
                    .patientId(patientId)
                    .note(request.getContent())
                    .createdAt(LocalDateTime.now())
                    .build();

            when(patientNoteRepository.save(any(PatientNote.class))).thenReturn(saved);

            PatientNoteResponseDTO response = patientNoteService.addNote(patientId, request);

            assertNotNull(response);
            assertEquals(saved.getId(), response.getId());
            assertEquals(saved.getPatientId(), response.getPatientId());
            assertEquals(saved.getNote(), response.getNote());
            assertEquals(saved.getCreatedAt(), response.getCreatedAt());

            verify(patientNoteRepository, times(1)).save(any(PatientNote.class));
        }

        @Test
        void getHistory_shouldReturnMappedList() {
            Long patientId = 456L;
            List<PatientNote> notes = new ArrayList<>();
            notes.add(PatientNote.builder().id("n1").patientId(patientId).note("one").createdAt(LocalDateTime.now()).build());
            notes.add(PatientNote.builder().id("n2").patientId(patientId).note("two").createdAt(LocalDateTime.now()).build());

            when(patientNoteRepository.findByPatientId(patientId)).thenReturn(notes);

            List<PatientNoteResponseDTO> history = patientNoteService.getHistory(patientId);

            assertNotNull(history);
            assertEquals(2, history.size());
            assertEquals("n1", history.get(0).getId());
            assertEquals("one", history.get(0).getNote());
            assertEquals("n2", history.get(1).getId());
            assertEquals("two", history.get(1).getNote());

            verify(patientNoteRepository, times(1)).findByPatientId(patientId);
        }
    }
}
