package com.Medilabo_solutions.Evaluation_Service.service;

import com.Medilabo_solutions.Evaluation_Service.dto.PatientNoteResponseDTO;
import com.Medilabo_solutions.Evaluation_Service.dto.PatientResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires pour PatientHistoryClientServiceImpl")
public class PatientHistoryClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PatientHistoryClientServiceImpl patientHistoryClientServiceImpl;

    private static final String TOKEN = "Bearer test-token-123";
    private static final String BASE_URL = "http://localhost:8080/api/v1/history/patient";
    private static final Long PATIENT_ID = 1L;
    private PatientNoteResponseDTO patientNoteResponse;

    @BeforeEach
    void setUp() {
        patientNoteResponse = new PatientNoteResponseDTO();
        patientNoteResponse.setId("1");
        patientNoteResponse.setPatientId(PATIENT_ID);
        patientNoteResponse.setNote("Patient has a history of hypertension.");
        patientNoteResponse.setCreatedAt(LocalDate.now().atStartOfDay());

    }

    @Nested
    @DisplayName("Récupération réussie d'un historique de patient")
    class SuccessfulPatientHistoryRetrievalTests {

        @Test
        @DisplayName("Devrait retourner un historique de patient avec succès")
        void shouldReturnPatientHistorySuccessfully() {
            // Arrange
            HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders() {{
                set("Authorization", TOKEN);
            }});
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);

            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            List<PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, TOKEN);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(patientNoteResponse.getId(), result.getFirst().getId());
            assertEquals(patientNoteResponse.getPatientId(), result.getFirst().getPatientId());
            assertEquals(patientNoteResponse.getNote(), result.getFirst().getNote());
            assertEquals(patientNoteResponse.getCreatedAt(), result.getFirst().getCreatedAt());

            verify(restTemplate).exchange(
                    BASE_URL + "/id/" + PATIENT_ID,
                    HttpMethod.GET,
                    entity,
                    PatientNoteResponseDTO[].class);
        }
        @Test
        @DisplayName("Devrait utiliser le bon token dans les headers")
        void shouldSetCorrectAuthorizationHeader() {
            // Arrange
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);


            String customToken = "Bearer custom-token-456";
            when(restTemplate.exchange(
                    contains(PATIENT_ID.toString()),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
           List <PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, customToken);

            // Assert
            assertNotNull(result);
            verify(restTemplate).exchange(
                    contains(PATIENT_ID.toString()),
                    eq(HttpMethod.GET),
                    argThat(entity -> {
                        var authHeader = entity.getHeaders().get("Authorization");
                        return authHeader != null && authHeader.getFirst().equals(customToken);
                    }),
                    eq(PatientNoteResponseDTO[].class)
            );
        }
        @Test
        @DisplayName("Devrait construire l'URL correctement")
        void shouldConstructCorrectUrl() {
            // Arrange
            long testPatientId = 42L;
            PatientNoteResponseDTO testPatient = new PatientNoteResponseDTO();
            testPatient.setId(String.valueOf(testPatientId));
            HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders() {{
                set("Authorization", TOKEN);
            }});
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);


            when(restTemplate.exchange(
                    any(String.class),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            patientHistoryClientServiceImpl.getPatientNotesByPatientId(testPatientId, TOKEN);

            // Assert
            verify(restTemplate).exchange(
                    BASE_URL + "/id/" + testPatientId,
                    HttpMethod.GET,
                    entity,
                    PatientNoteResponseDTO[].class

            );
        }



    }
    @Nested
    @DisplayName("Gestion des erreurs")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Devrait lever une RuntimeException en cas d'erreur RestTemplate")
        void shouldThrowRuntimeExceptionOnRestTemplateError() {
            // Arrange
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO.class)
            )).thenThrow(new RuntimeException("Service unavailable"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, TOKEN);
            });

            assertTrue(exception.getMessage().contains("Failed to retrieve patient notes"));
            assertTrue(exception.getMessage().contains(PATIENT_ID.toString()));
        }

        @Test
        @DisplayName("Devrait préserver le message d'erreur original")
        void shouldPreserveOriginalErrorMessage() {
            // Arrange
            String originalErrorMessage = "Connection timeout";
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenThrow(new RestClientException(originalErrorMessage));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, TOKEN);
            });

            assertTrue(exception.getCause().getMessage().contains(originalErrorMessage));
        }

        @Test
        @DisplayName("Devrait gérer une réponse nulle")
        void shouldHandleNullResponse() {
            // Arrange

            ResponseEntity<PatientNoteResponseDTO[]> responseEntity = new ResponseEntity<>(new PatientNoteResponseDTO[0], HttpStatus.OK);


            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
           List <PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, TOKEN);

            // Assert
            assertTrue(result.isEmpty());  }
    }

    @Nested
    @DisplayName("Cas limites")
    class EdgeCasesTests {

        @Test
        @DisplayName("Devrait gérer les IDs de patient très grands")
        void shouldHandleLargePatientIds() {
            // Arrange
            Long largePatientId = 9999999999L;
            PatientNoteResponseDTO testNote = new PatientNoteResponseDTO();
            testNote.setId(String.valueOf(largePatientId));
            testNote.setPatientId(largePatientId);
            testNote.setNote("Test note");

            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{testNote}, HttpStatus.OK);

            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            List<PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(largePatientId, TOKEN);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(largePatientId, result.getFirst().getPatientId());
        }

        @Test
        @DisplayName("Devrait gérer les tokens vides")
        void shouldHandleEmptyToken() {
            // Arrange
            String emptyToken = "";
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);

            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            List<PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, emptyToken);

            // Assert
            assertNotNull(result);
            verify(restTemplate).exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    argThat(entity -> {
                        var authHeader = entity.getHeaders().get("Authorization");
                        return authHeader != null && authHeader.getFirst().equals(emptyToken);
                    }),
                    eq(PatientNoteResponseDTO[].class)
            );
        }

        @Test
        @DisplayName("Devrait gérer les tokens spéciaux")
        void shouldHandleSpecialCharactersInToken() {
            // Arrange
            String specialToken = "Bearer token!@#$%^&*()_+-=[]{}|;:,.<>?";
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);

            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            List<PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, specialToken);

            // Assert
            assertNotNull(result);
            verify(restTemplate).exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    argThat(entity -> {
                        var authHeader = entity.getHeaders().get("Authorization");
                        return authHeader != null && authHeader.getFirst().equals(specialToken);
                    }),
                    eq(PatientNoteResponseDTO[].class)
            );
        }

        @Test
        @DisplayName("Devrait utiliser la méthode HTTP GET")
        void shouldUseGetHttpMethod() {
            // Arrange
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);

            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, TOKEN);

            // Assert
            verify(restTemplate).exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            );
        }
    }

    @Nested
    @DisplayName("Validation des données retournées")
    class ResponseValidationTests {

        @Test
        @DisplayName("Devrait retourner tous les champs de la note de patient")
        void shouldReturnCompletePatientNoteData() {
            // Arrange
            patientNoteResponse.setNote("Updated note with more details");
            
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);

            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            List<PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, TOKEN);

            // Assert
            assertAll(
                    () -> assertEquals(1, result.size()),
                    () -> assertEquals("1", result.getFirst().getId()),
                    () -> assertEquals(PATIENT_ID, result.getFirst().getPatientId()),
                    () -> assertEquals("Updated note with more details", result.getFirst().getNote()),
                    () -> assertNotNull(result.getFirst().getCreatedAt())
            );
        }

        @Test
        @DisplayName("Devrait conserver les références d'objet")
        void shouldReturnSameObjectInstance() {
            // Arrange
            ResponseEntity<PatientNoteResponseDTO[]> responseEntity =
                    new ResponseEntity<>(new PatientNoteResponseDTO[]{patientNoteResponse}, HttpStatus.OK);

            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientNoteResponseDTO[].class)
            )).thenReturn(responseEntity);

            // Act
            List<PatientNoteResponseDTO> result = patientHistoryClientServiceImpl.getPatientNotesByPatientId(PATIENT_ID, TOKEN);

            // Assert
            assertEquals(1, result.size());
            assertSame(patientNoteResponse, result.getFirst());
        }
    }
}
