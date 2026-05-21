package com.Medilabo_solutions.Evaluation_Service.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires pour PatientClientServiceImpl")
class PatientClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PatientClientServiceImpl patientClientService;

    private static final String TOKEN = "Bearer test-token-123";
    private static final String BASE_URL = "http://localhost:8080/api/v1/patients";
    private static final Long PATIENT_ID = 1L;

    private PatientResponseDTO patientResponse;

    @BeforeEach
    void setUp() {
        patientResponse = new PatientResponseDTO();
        patientResponse.setId(PATIENT_ID);
        patientResponse.setFirstName("John");
        patientResponse.setLastName("Doe");
        patientResponse.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patientResponse.setGender("M");
        patientResponse.setAddress("123 Main St");
        patientResponse.setPhoneNumber("555-0123");
    }

    @Nested
    @DisplayName("Récupération réussie d'un patient")
    class SuccessfulPatientRetrievalTests {

        @Test
        @DisplayName("Devrait retourner un patient valide")
        void shouldReturnPatientSuccessfully() {
            // Arrange
            HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders() {{
                set("Authorization", TOKEN);
            }});
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(patientResponse, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(PATIENT_ID, TOKEN);

            // Assert
            assertNotNull(result);
            assertEquals(PATIENT_ID, result.getId());
            assertEquals("John", result.getFirstName());
            assertEquals("Doe", result.getLastName());
            assertEquals("M", result.getGender());
            assertEquals(LocalDate.of(1990, 5, 15), result.getDateOfBirth());
            assertEquals("123 Main St", result.getAddress());
            assertEquals("555-0123", result.getPhoneNumber());

            verify(restTemplate).exchange(
                    BASE_URL + "/id/" + PATIENT_ID,
                    HttpMethod.GET,
                   entity,
                    PatientResponseDTO.class);
        }

        @Test
        @DisplayName("Devrait utiliser le bon token dans les headers")
        void shouldSetCorrectAuthorizationHeader() {
            // Arrange
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(patientResponse, HttpStatus.OK);
            
            String customToken = "Bearer custom-token-456";
            when(restTemplate.exchange(
                    contains(PATIENT_ID.toString()),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(PATIENT_ID, customToken);

            // Assert
            assertNotNull(result);
            verify(restTemplate).exchange(
                    contains(PATIENT_ID.toString()),
                    eq(HttpMethod.GET),
                    argThat(entity -> {
                        var authHeader = entity.getHeaders().get("Authorization");
                        return authHeader != null && authHeader.getFirst().equals(customToken);
                    }),
                    eq(PatientResponseDTO.class)
            );
        }

        @Test
        @DisplayName("Devrait construire l'URL correctement")
        void shouldConstructCorrectUrl() {
            // Arrange
            long testPatientId = 42L;
            PatientResponseDTO testPatient = new PatientResponseDTO();
            testPatient.setId(testPatientId);
            HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders() {{
                set("Authorization", TOKEN);
            }});
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(testPatient, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    any(String.class),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            patientClientService.getPatientById(testPatientId, TOKEN);

            // Assert
            verify(restTemplate).exchange(
                    BASE_URL + "/id/" + testPatientId,
                    HttpMethod.GET,
                    entity,
                    PatientResponseDTO.class


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
                    eq(PatientResponseDTO.class)
            )).thenThrow(new RestClientException("Service unavailable"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                patientClientService.getPatientById(PATIENT_ID, TOKEN);
            });

            assertTrue(exception.getMessage().contains("Failed to retrieve patient"));
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
                    eq(PatientResponseDTO.class)
            )).thenThrow(new RestClientException(originalErrorMessage));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                patientClientService.getPatientById(PATIENT_ID, TOKEN);
            });

            assertTrue(exception.getCause().getMessage().contains(originalErrorMessage));
        }

        @Test
        @DisplayName("Devrait gérer une réponse nulle")
        void shouldHandleNullResponse() {
            // Arrange
            PatientResponseDTO nullPatient = null;
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(nullPatient, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(PATIENT_ID, TOKEN);

            // Assert
            assertNull(result);
        }
    }

    @Nested
    @DisplayName("Cas limites")
    class EdgeCasesTests {

        @Test
        @DisplayName("Devrait gérer les IDs de patient très grands")
        void shouldHandleLargePatientIds() {
            // Arrange
            Long largePatientId = 9999999999L;
            PatientResponseDTO testPatient = new PatientResponseDTO();
            testPatient.setId(largePatientId);
            
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(testPatient, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(largePatientId, TOKEN);

            // Assert
            assertNotNull(result);
            assertEquals(largePatientId, result.getId());
        }

        @Test
        @DisplayName("Devrait gérer les tokens vides")
        void shouldHandleEmptyToken() {
            // Arrange
            String emptyToken = "";
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(patientResponse, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(PATIENT_ID, emptyToken);

            // Assert
            assertNotNull(result);
            verify(restTemplate).exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    argThat(entity -> {
                        var authHeader = entity.getHeaders().get("Authorization");
                        return authHeader != null && authHeader.getFirst().equals(emptyToken);
                    }),
                    eq(PatientResponseDTO.class)
            );
        }

        @Test
        @DisplayName("Devrait gérer les tokens spéciaux")
        void shouldHandleSpecialCharactersInToken() {
            // Arrange
            String specialToken = "Bearer token!@#$%^&*()_+-=[]{}|;:,.<>?";
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(patientResponse, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(PATIENT_ID, specialToken);

            // Assert
            assertNotNull(result);
            verify(restTemplate).exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    argThat(entity -> {
                        var authHeader = entity.getHeaders().get("Authorization");
                        return authHeader != null && authHeader.getFirst().equals(specialToken);
                    }),
                    eq(PatientResponseDTO.class)
            );
        }

        @Test
        @DisplayName("Devrait utiliser la méthode HTTP GET")
        void shouldUseGetHttpMethod() {
            // Arrange
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(patientResponse, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            patientClientService.getPatientById(PATIENT_ID, TOKEN);

            // Assert
            verify(restTemplate).exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            );
        }
    }

    @Nested
    @DisplayName("Validation des données retournées")
    class ResponseValidationTests {

        @Test
        @DisplayName("Devrait retourner tous les champs du patient")
        void shouldReturnCompletePatientData() {
            // Arrange
            patientResponse.setFirstName("Jane");
            patientResponse.setLastName("Smith");
            patientResponse.setGender("F");
            patientResponse.setAddress("456 Oak Ave");
            patientResponse.setPhoneNumber("555-9876");
            patientResponse.setDateOfBirth(LocalDate.of(1992, 3, 20));
            
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(patientResponse, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(PATIENT_ID, TOKEN);

            // Assert
            assertAll(
                () -> assertEquals(PATIENT_ID, result.getId()),
                () -> assertEquals("Jane", result.getFirstName()),
                () -> assertEquals("Smith", result.getLastName()),
                () -> assertEquals("F", result.getGender()),
                () -> assertEquals("456 Oak Ave", result.getAddress()),
                () -> assertEquals("555-9876", result.getPhoneNumber()),
                () -> assertEquals(LocalDate.of(1992, 3, 20), result.getDateOfBirth())
            );
        }

        @Test
        @DisplayName("Devrait conserver les références d'objet")
        void shouldReturnSameObjectInstance() {
            // Arrange
            ResponseEntity<PatientResponseDTO> responseEntity = 
                new ResponseEntity<>(patientResponse, HttpStatus.OK);
            
            when(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(PatientResponseDTO.class)
            )).thenReturn(responseEntity);

            // Act
            PatientResponseDTO result = patientClientService.getPatientById(PATIENT_ID, TOKEN);

            // Assert
            assertSame(patientResponse, result);
        }
    }
}
