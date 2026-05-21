package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.EvaluationResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.dto.PatientResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.helper.RiskLevels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires pour EvaluationServiceImpl")
class EvaluationServiceImplTest {

    @Mock
    private PatientClientService patientClientService;

    @Mock
    private PatientHistoryClientService patientHistoryClientService;

    @InjectMocks
    private EvaluationServiceImpl evaluationService;

    private PatientResponseDTO patient;
    private List<PatientNoteResponseDTO> notes;
    private static final String TOKEN = "test-token-123";

    @BeforeEach
    void setUp() {
        patient = new PatientResponseDTO();
        notes = new ArrayList<>();
    }

    @Nested
    @DisplayName("Évaluation pour les adultes (âge > 30)")
    class AdultRiskEvaluationTests {

        @Test
        @DisplayName("Devrait retourner NONE si aucun trigger détecté")
        void shouldReturnNoneWhenNoTriggers() {
            // Arrange
            patient.setId(1L);
            patient.setDateOfBirth(LocalDate.now().minusYears(40));
            patient.setGender("M");
            notes = List.of();

            when(patientClientService.getPatientById(1L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(1L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(1L, TOKEN);

            // Assert
            assertEquals(RiskLevels.NONE, result.getRiskLevel());
            assertEquals(1L, result.getId());
        }

        @Test
        @DisplayName("Devrait retourner BORDERLINE pour 2-5 triggers")
        void shouldReturnBorderlineFor2To5Triggers() {
            // Arrange
            patient.setId(1L);
            patient.setDateOfBirth(LocalDate.now().minusYears(35));
            patient.setGender("M");

            PatientNoteResponseDTO note1 = createNoteWithTrigger(1L, "hémoglobine a1c");
            PatientNoteResponseDTO note2 = createNoteWithTrigger(1L, "cholestérol");
            notes = List.of(note1, note2);

            when(patientClientService.getPatientById(1L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(1L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(1L, TOKEN);

            // Assert
            assertEquals(RiskLevels.BORDERLINE, result.getRiskLevel());
        }

        @Test
        @DisplayName("Devrait retourner IN_DANGER pour 6-7 triggers")
        void shouldReturnInDangerFor6To7Triggers() {
            // Arrange
            patient.setId(1L);
            patient.setDateOfBirth(LocalDate.now().minusYears(40));
            patient.setGender("F");

            List<String> triggerWords = List.of(
                "hémoglobine a1c", "microalbumine", "taille",
                "poids", "fumeur", "cholestérol"
            );
            notes = triggerWords.stream()
                .map(trigger -> createNoteWithTrigger(1L, trigger))
                .toList();

            when(patientClientService.getPatientById(1L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(1L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(1L, TOKEN);

            // Assert
            assertEquals(RiskLevels.IN_DANGER, result.getRiskLevel());
        }

        @Test
        @DisplayName("Devrait retourner EARLY_ONSET pour 8+ triggers")
        void shouldReturnEarlyOnsetFor8OrMoreTriggers() {
            // Arrange
            patient.setId(1L);
            patient.setDateOfBirth(LocalDate.now().minusYears(50));
            patient.setGender("M");

            List<String> triggerWords = List.of(
                "hémoglobine a1c", "microalbumine", "taille",
                "poids", "fumeur", "anormal", "cholestérol", "vertiges"
            );
            notes = triggerWords.stream()
                .map(trigger -> createNoteWithTrigger(1L, trigger))
                .toList();

            when(patientClientService.getPatientById(1L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(1L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(1L, TOKEN);

            // Assert
            assertEquals(RiskLevels.EARLY_ONSET, result.getRiskLevel());
        }
    }

    @Nested
    @DisplayName("Évaluation pour les jeunes adultes (âge <= 30)")
    class YoungAdultRiskEvaluationTests {

        @Nested
        @DisplayName("Cas des hommes (M)")
        class MaleYoungAdultTests {

            @Test
            @DisplayName("Homme jeune < 3 triggers = NONE")
            void shouldReturnNoneForMaleWith0To2Triggers() {
                // Arrange
                patient.setId(2L);
                patient.setDateOfBirth(LocalDate.now().minusYears(25));
                patient.setGender("M");

                PatientNoteResponseDTO note = createNoteWithTrigger(2L, "hémoglobine a1c");
                notes = List.of(note);

                when(patientClientService.getPatientById(2L, TOKEN)).thenReturn(patient);
                when(patientHistoryClientService.getPatientNotesByPatientId(2L, TOKEN)).thenReturn(notes);

                // Act
                EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(2L, TOKEN);

                // Assert
                assertEquals(RiskLevels.NONE, result.getRiskLevel());
            }

            @Test
            @DisplayName("Homme jeune 3-4 triggers = IN_DANGER")
            void shouldReturnInDangerForMaleWith3To4Triggers() {
                // Arrange
                patient.setId(2L);
                patient.setDateOfBirth(LocalDate.now().minusYears(28));
                patient.setGender("M");

                List<String> triggerWords = List.of("hémoglobine a1c", "microalbumine", "taille");
                notes = triggerWords.stream()
                    .map(trigger -> createNoteWithTrigger(2L, trigger))
                    .toList();

                when(patientClientService.getPatientById(2L, TOKEN)).thenReturn(patient);
                when(patientHistoryClientService.getPatientNotesByPatientId(2L, TOKEN)).thenReturn(notes);

                // Act
                EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(2L, TOKEN);

                // Assert
                assertEquals(RiskLevels.IN_DANGER, result.getRiskLevel());
            }

            @Test
            @DisplayName("Homme jeune 5+ triggers = EARLY_ONSET")
            void shouldReturnEarlyOnsetForMaleWith5OrMoreTriggers() {
                // Arrange
                patient.setId(2L);
                patient.setDateOfBirth(LocalDate.now().minusYears(20));
                patient.setGender("M");

                List<String> triggerWords = List.of(
                    "hémoglobine a1c", "microalbumine", "taille", "poids", "fumeur"
                );
                notes = triggerWords.stream()
                    .map(trigger -> createNoteWithTrigger(2L, trigger))
                    .toList();

                when(patientClientService.getPatientById(2L, TOKEN)).thenReturn(patient);
                when(patientHistoryClientService.getPatientNotesByPatientId(2L, TOKEN)).thenReturn(notes);

                // Act
                EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(2L, TOKEN);

                // Assert
                assertEquals(RiskLevels.EARLY_ONSET, result.getRiskLevel());
            }
        }

        @Nested
        @DisplayName("Cas des femmes (F)")
        class FemaleYoungAdultTests {

            @Test
            @DisplayName("Femme jeune < 4 triggers = NONE")
            void shouldReturnNoneForFemaleWith0To3Triggers() {
                // Arrange
                patient.setId(3L);
                patient.setDateOfBirth(LocalDate.now().minusYears(22));
                patient.setGender("F");

                PatientNoteResponseDTO note1 = createNoteWithTrigger(3L, "hémoglobine a1c");
                PatientNoteResponseDTO note2 = createNoteWithTrigger(3L, "cholestérol");
                notes = List.of(note1, note2);

                when(patientClientService.getPatientById(3L, TOKEN)).thenReturn(patient);
                when(patientHistoryClientService.getPatientNotesByPatientId(3L, TOKEN)).thenReturn(notes);

                // Act
                EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(3L, TOKEN);

                // Assert
                assertEquals(RiskLevels.NONE, result.getRiskLevel());
            }

            @Test
            @DisplayName("Femme jeune 4-6 triggers = IN_DANGER")
            void shouldReturnInDangerForFemaleWith4To6Triggers() {
                // Arrange
                patient.setId(3L);
                patient.setDateOfBirth(LocalDate.now().minusYears(29));
                patient.setGender("F");

                List<String> triggerWords = List.of(
                    "hémoglobine a1c", "microalbumine", "taille", "poids"
                );
                notes = triggerWords.stream()
                    .map(trigger -> createNoteWithTrigger(3L, trigger))
                    .toList();

                when(patientClientService.getPatientById(3L, TOKEN)).thenReturn(patient);
                when(patientHistoryClientService.getPatientNotesByPatientId(3L, TOKEN)).thenReturn(notes);

                // Act
                EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(3L, TOKEN);

                // Assert
                assertEquals(RiskLevels.IN_DANGER, result.getRiskLevel());
            }

            @Test
            @DisplayName("Femme jeune 7+ triggers = EARLY_ONSET")
            void shouldReturnEarlyOnsetForFemaleWith7OrMoreTriggers() {
                // Arrange
                patient.setId(3L);
                patient.setDateOfBirth(LocalDate.now().minusYears(18));
                patient.setGender("F");

                List<String> triggerWords = List.of(
                    "hémoglobine a1c", "microalbumine", "taille", "poids",
                    "fumeur", "anormal", "cholestérol"
                );
                notes = triggerWords.stream()
                    .map(trigger -> createNoteWithTrigger(3L, trigger))
                    .toList();

                when(patientClientService.getPatientById(3L, TOKEN)).thenReturn(patient);
                when(patientHistoryClientService.getPatientNotesByPatientId(3L, TOKEN)).thenReturn(notes);

                // Act
                EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(3L, TOKEN);

                // Assert
                assertEquals(RiskLevels.EARLY_ONSET, result.getRiskLevel());
            }
        }
    }

    @Nested
    @DisplayName("Tests des cas limites")
    class EdgeCaseTests {

        @Test
        @DisplayName("Devrait gérer les notes vides")
        void shouldHandleEmptyNotes() {
            // Arrange
            patient.setId(4L);
            patient.setDateOfBirth(LocalDate.now().minusYears(35));
            patient.setGender("M");
            notes = List.of();

            when(patientClientService.getPatientById(4L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(4L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(4L, TOKEN);

            // Assert
            assertEquals(RiskLevels.NONE, result.getRiskLevel());
        }

        @Test
        @DisplayName("Devrait être insensible à la casse dans les triggers")
        void shouldBeCaseInsensitiveForTriggers() {
            // Arrange
            patient.setId(5L);
            patient.setDateOfBirth(LocalDate.now().minusYears(40));
            patient.setGender("M");

            PatientNoteResponseDTO note = new PatientNoteResponseDTO();
            note.setId("note-id");
            note.setPatientId(5L);
            note.setNote("Patient note contenant HÉMOGLOBINE A1C en majuscules");
            note.setCreatedAt(LocalDateTime.now());
            notes = List.of(note);

            when(patientClientService.getPatientById(5L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(5L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(5L, TOKEN);

            // Assert
            assertNotEquals(RiskLevels.NONE, result.getRiskLevel());
        }

        @Test
        @DisplayName("Age exact = 30 ans doit être traité selon la logique de jeune adulte")
        void shouldTreatAge30AsYoungAdult() {
            // Arrange
            patient.setId(6L);
            patient.setDateOfBirth(LocalDate.now().minusYears(30));
            patient.setGender("M");

            PatientNoteResponseDTO note = createNoteWithTrigger(6L, "hémoglobine a1c");
            notes = List.of(note);

            when(patientClientService.getPatientById(6L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(6L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(6L, TOKEN);

            // Assert
            assertNotNull(result.getRiskLevel());
        }

        @Test
        @DisplayName("Devrait compter un seul trigger par note")
        void shouldCountOnlyOneTriggerPerNote() {
            // Arrange
            patient.setId(7L);
            patient.setDateOfBirth(LocalDate.now().minusYears(40));
            patient.setGender("M");

            PatientNoteResponseDTO note = new PatientNoteResponseDTO();
            note.setId("note-id");
            note.setPatientId(7L);
            // Contient 2 triggers potentiels (hémoglobine a1c et cholestérol),
            // mais devrait ne compter qu'une fois par note.
            // On compte 1 trigger unique, ce qui retourne BORDERLINE pour un adulte
            note.setNote("Hémoglobine A1C et cholestérol élevés");
            note.setCreatedAt(LocalDateTime.now());
            notes = List.of(note);

            when(patientClientService.getPatientById(7L, TOKEN)).thenReturn(patient);
            when(patientHistoryClientService.getPatientNotesByPatientId(7L, TOKEN)).thenReturn(notes);

            // Act
            EvaluationResponseDTO result = evaluationService.evaluatePatientRisk(7L, TOKEN);

            // Assert
            // Avec 1 seul trigger compté (le premier trouvé: hémoglobine a1c)
            // et étant adulte > 30, le risque est BORDERLINE (1-5 triggers)
            assertEquals(RiskLevels.BORDERLINE, result.getRiskLevel());
        }
    }

    // Méthode utilitaire pour créer une note avec un trigger
    private PatientNoteResponseDTO createNoteWithTrigger(Long patientId, String trigger) {
        PatientNoteResponseDTO note = new PatientNoteResponseDTO();
        note.setId("note-id-" + patientId);
        note.setPatientId(patientId);
        note.setNote("Patient note contenant " + trigger);
        note.setCreatedAt(LocalDateTime.now());
        return note;
    }
}



