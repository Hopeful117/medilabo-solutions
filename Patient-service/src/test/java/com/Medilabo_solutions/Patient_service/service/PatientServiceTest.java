package com.Medilabo_solutions.Patient_service.service;

import com.Medilabo_solutions.Patient_service.exception.DuplicateResourceException;
import com.Medilabo_solutions.Patient_service.exception.ResourceNotFoundException;
import com.Medilabo_solutions.Patient_service.exception.InvalidDateException;
import com.Medilabo_solutions.Patient_service.model.Patient;
import com.Medilabo_solutions.Patient_service.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PatientServiceImpl Tests")
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient testPatient;
    private Patient secondPatient;

    /**
     * Set up common test data before each test case
     * */
    @BeforeEach
    void setUp() {
        testPatient = new Patient();
        testPatient.setId(1);
        testPatient.setFirstName("John");
        testPatient.setLastName("Doe");
        testPatient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        testPatient.setGender(Patient.Gender.M);
        testPatient.setAddress("123 Main St, Paris");
        testPatient.setPhoneNumber("01-23-45-67-89");

        secondPatient = new Patient();
        secondPatient.setId(2);
        secondPatient.setFirstName("Jane");
        secondPatient.setLastName("Smith");
        secondPatient.setDateOfBirth(LocalDate.of(1992, 8, 20));
        secondPatient.setGender(Patient.Gender.F);
        secondPatient.setAddress("456 Oak Ave, Lyon");
        secondPatient.setPhoneNumber("02-34-56-78-90");
    }

    /**
     * Test retrieving all patients successfully.
     */
    @Test
    @DisplayName("Should retrieve all patients successfully")
    void testGetAllPatients() {

        List<Patient> patients = Arrays.asList(testPatient, secondPatient);
        when(patientRepository.findAll()).thenReturn(patients);


        List<Patient> result = patientService.getAllPatients();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(patientRepository, times(1)).findAll();
    }
    /**
     * Test retrieving all patients when no patients exist.
     */
    @Test
    @DisplayName("Should retrieve an empty list when no patients exist")
    void testGetAllPatientsEmpty() {

        when(patientRepository.findAll()).thenReturn(Arrays.asList());


        List<Patient> result = patientService.getAllPatients();


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository, times(1)).findAll();
    }

    /**
     * Test retrieving a patient by ID successfully.
     */
    @Test
    @DisplayName("Should retrieve patient by ID successfully")
    void testGetPatientById_Success() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));


        Patient result = patientService.getPatientById(1L);


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(patientRepository, times(1)).findById(1L);
    }

    /**
     * Test retrieving a patient by ID when the patient does not exist.
     * Should throw ResourceNotFoundException.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when patient not found by ID")
    void testGetPatientById_NotFound() {
        // Arrange
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getPatientById(999L);
        });
        verify(patientRepository, times(1)).findById(999L);
    }
    /**
     * Test creating a new patient successfully.
     */
    @Test
    @DisplayName("Should create a new patient successfully")
    void testCreatePatient_Success() {

        Patient newPatient = new Patient();
        newPatient.setFirstName("Michael");
        newPatient.setLastName("Johnson");
        newPatient.setDateOfBirth(LocalDate.of(1985, 3, 10));
        newPatient.setGender(Patient.Gender.M);
        newPatient.setAddress("789 Pine St");
        newPatient.setPhoneNumber("03-45-67-89-00");

        when(patientRepository.existsByLastNameAndFirstName("Johnson", "Michael")).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(newPatient);


        Patient result = patientService.createPatient(newPatient);


        assertNotNull(result);
        assertEquals("Michael", result.getFirstName());
        assertEquals("Johnson", result.getLastName());
        verify(patientRepository, times(1)).existsByLastNameAndFirstName("Johnson", "Michael");
        verify(patientRepository, times(1)).save(newPatient);
    }
    /**
     * Test creating a duplicate patient.
     * Should throw DuplicateResourceException.
     */
    @Test
    @DisplayName("Should throw DuplicateResourceException when creating duplicate patient")
    void testCreatePatient_Duplicate() {
        // Arrange
        Patient duplicatePatient = new Patient();
        duplicatePatient.setFirstName("John");
        duplicatePatient.setLastName("Doe");

        when(patientRepository.existsByLastNameAndFirstName("Doe", "John")).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            patientService.createPatient(duplicatePatient);
        });

        assertTrue(exception.getMessage().contains("Patient with name John Doe already exists"));
        verify(patientRepository, times(1)).existsByLastNameAndFirstName("Doe", "John");
        verify(patientRepository, never()).save(any(Patient.class));
    }
    /**
     * Test updating an existing patient successfully.
     */
    @Test
    @DisplayName("Should update an existing patient successfully")
    void testUpdatePatient_Success() {

        Patient updatedData = new Patient();
        updatedData.setFirstName("Jonathan");
        updatedData.setLastName("DoeUpdated");
        updatedData.setDateOfBirth(LocalDate.of(1991, 5, 15));
        updatedData.setGender(Patient.Gender.M);
        updatedData.setAddress("999 Updated St");
        updatedData.setPhoneNumber("04-56-78-90-01");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);


        Patient result = patientService.updatePatient(1L, updatedData);


        assertNotNull(result);
        assertEquals("Jonathan", result.getFirstName());
        assertEquals("DoeUpdated", result.getLastName());
        assertEquals("999 Updated St", result.getAddress());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(testPatient);
    }
    /**
     * Test updating a non-existent patient.
     * Should throw ResourceNotFoundException.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent patient")
    void testUpdatePatient_NotFound() {

        Patient updatedData = new Patient();
        updatedData.setFirstName("Test");
        updatedData.setLastName("Test");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            patientService.updatePatient(999L, updatedData);
        });
        verify(patientRepository, times(1)).findById(999L);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    /**
     * Test deleting a patient successfully.
     */
    @Test
    @DisplayName("Should delete a patient successfully")
    void testDeletePatient_Success() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        doNothing().when(patientRepository).delete(any(Patient.class));


        patientService.deletePatient(1L);


        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).delete(testPatient);
    }
    /**
     * Test deleting a non-existent patient.
     * Should throw ResourceNotFoundException.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent patient")
    void testDeletePatient_NotFound() {

        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            patientService.deletePatient(999L);
        });
        verify(patientRepository, times(1)).findById(999L);
        verify(patientRepository, never()).delete(any(Patient.class));
    }
    /**
     * Test retrieving patients by last name successfully.
     */
    @Test
    @DisplayName("Should retrieve patients by last name successfully")
    void testGetPatientsByLastName_Success() {
        // Arrange
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.findByLastName("Doe")).thenReturn(patients);

        // Act
        List<Patient> result = patientService.getPatientsByLastName("Doe");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        verify(patientRepository, times(1)).findByLastName("Doe");
    }
    /**
     * Test retrieving patients by last name when no patients are found.
     * Should return an empty list.
     */
    @Test
    @DisplayName("Should return empty list when no patients found by last name")
    void testGetPatientsByLastName_Empty() {
        // Arrange
        when(patientRepository.findByLastName("Unknown")).thenReturn(Arrays.asList());

        // Act
        List<Patient> result = patientService.getPatientsByLastName("Unknown");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository, times(1)).findByLastName("Unknown");
    }
    /**
     * Test retrieving multiple patients by last name successfully.
     */
    @Test
    @DisplayName("Should retrieve multiple patients by last name")
    void testGetPatientsByLastName_Multiple() {

        Patient anotherSameName = new Patient();
        anotherSameName.setId(3);
        anotherSameName.setFirstName("Jack");
        anotherSameName.setLastName("Doe");

        List<Patient> patients = Arrays.asList(testPatient, anotherSameName);
        when(patientRepository.findByLastName("Doe")).thenReturn(patients);


        List<Patient> result = patientService.getPatientsByLastName("Doe");


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jack", result.get(1).getFirstName());
        verify(patientRepository, times(1)).findByLastName("Doe");
    }
    /**
     * Test retrieving a patient by last name and first name successfully.
     */
    @Test
    @DisplayName("Should retrieve patient by last name and first name successfully")
    void testGetPatientByLastNameAndFirstName_Success() {

        when(patientRepository.findByLastNameAndFirstName("Doe", "John"))
                .thenReturn(Optional.of(testPatient));


        Patient result = patientService.getPatientByLastNameAndFirstName("Doe", "John");


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(patientRepository, times(1)).findByLastNameAndFirstName("Doe", "John");
    }
    /**
     * Test retrieving a patient by last name and first name when the patient does not exist.
     * Should throw ResourceNotFoundException.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when patient not found by last and first name")
    void testGetPatientByLastNameAndFirstName_NotFound() {

        when(patientRepository.findByLastNameAndFirstName("Unknown", "Unknown"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getPatientByLastNameAndFirstName("Unknown", "Unknown");
        });

        assertTrue(exception.getMessage().contains("Patient not found with name: Unknown Unknown"));
        verify(patientRepository, times(1)).findByLastNameAndFirstName("Unknown", "Unknown");
    }
    /**
     * Test updating multiple fields of an existing patient successfully.
     */
    @Test
    @DisplayName("Should update multiple patient fields correctly")
    void testUpdatePatient_AllFields() {
        // Arrange
        Patient updatedData = new Patient();
        updatedData.setFirstName("UpdatedFirstName");
        updatedData.setLastName("UpdatedLastName");
        updatedData.setDateOfBirth(LocalDate.of(2000, 12, 25));
        updatedData.setGender(Patient.Gender.F);
        updatedData.setAddress("New Address");
        updatedData.setPhoneNumber("99-99-99-99-99");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);


        Patient result = patientService.updatePatient(1L, updatedData);


        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());
        assertEquals(LocalDate.of(2000, 12, 25), result.getDateOfBirth());
        assertEquals(Patient.Gender.F, result.getGender());
        assertEquals("New Address", result.getAddress());
        assertEquals("99-99-99-99-99", result.getPhoneNumber());
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
    /**
     * Test creating a patient with all fields populated successfully.
     */
    @Test
    @DisplayName("Should create patient with all fields populated")
    void testCreatePatient_AllFields() {
        // Arrange
        Patient newPatient = new Patient();
        newPatient.setFirstName("Alice");
        newPatient.setLastName("Wonder");
        newPatient.setDateOfBirth(LocalDate.of(1995, 7, 4));
        newPatient.setGender(Patient.Gender.F);
        newPatient.setAddress("123 Fantasy Lane");
        newPatient.setPhoneNumber("05-67-89-01-23");

        when(patientRepository.existsByLastNameAndFirstName("Wonder", "Alice")).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(newPatient);

        // Act
        Patient result = patientService.createPatient(newPatient);

        // Assert
        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Wonder", result.getLastName());
        assertEquals(LocalDate.of(1995, 7, 4), result.getDateOfBirth());
        assertEquals(Patient.Gender.F, result.getGender());
        assertEquals("123 Fantasy Lane", result.getAddress());
        assertEquals("05-67-89-01-23", result.getPhoneNumber());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test creating a patient with empty first name.
     * Should throw an exception during validation or save.
     */
    @Test
    @DisplayName("Should fail to create patient with empty first name")
    void testCreatePatient_EmptyFirstName() {
        // Arrange
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("");
        invalidPatient.setLastName("Test");
        invalidPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(patientRepository.existsByLastNameAndFirstName("Test", "")).thenReturn(false);
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new IllegalArgumentException("First name cannot be empty"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            patientService.createPatient(invalidPatient);
        });
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test creating a patient with empty last name.
     * Should throw an exception during validation or save.
     */
    @Test
    @DisplayName("Should fail to create patient with empty last name")
    void testCreatePatient_EmptyLastName() {
        // Arrange
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("David");
        invalidPatient.setLastName("");
        invalidPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(patientRepository.existsByLastNameAndFirstName("", "David")).thenReturn(false);
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new IllegalArgumentException("Last name cannot be empty"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            patientService.createPatient(invalidPatient);
        });
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test creating a patient when repository throws a DatabaseException.
     */
    @Test
    @DisplayName("Should fail to create patient when database error occurs")
    void testCreatePatient_DatabaseError() {
        // Arrange
        Patient newPatient = new Patient();
        newPatient.setFirstName("Robert");
        newPatient.setLastName("Brown");
        newPatient.setDateOfBirth(LocalDate.of(1988, 3, 15));

        when(patientRepository.existsByLastNameAndFirstName("Brown", "Robert")).thenReturn(false);
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.createPatient(newPatient);
        });
        verify(patientRepository, times(1)).existsByLastNameAndFirstName("Brown", "Robert");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test creating a patient with null date of birth.
     */
    @Test
    @DisplayName("Should fail to create patient with null date of birth")
    void testCreatePatient_NullDateOfBirth() {
        // Arrange
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("Sophie");
        invalidPatient.setLastName("Martin");
        invalidPatient.setDateOfBirth(null);

        when(patientRepository.existsByLastNameAndFirstName("Martin", "Sophie")).thenReturn(false);
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new IllegalArgumentException("Date of birth cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            patientService.createPatient(invalidPatient);
        });
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test updating a patient when save operation fails.
     */
    @Test
    @DisplayName("Should fail to update patient when save operation fails")
    void testUpdatePatient_SaveError() {
        // Arrange
        Patient updatedData = new Patient();
        updatedData.setFirstName("UpdatedFail");
        updatedData.setLastName("UpdatedFail");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new RuntimeException("Database save failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            patientService.updatePatient(1L, updatedData);
        });
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test updating a patient with invalid data (empty firstName).
     */
    @Test
    @DisplayName("Should fail to update patient with invalid data (empty firstName)")
    void testUpdatePatient_InvalidFirstName() {
        // Arrange
        Patient updatedData = new Patient();
        updatedData.setFirstName("");
        updatedData.setLastName("ValidName");
        updatedData.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new IllegalArgumentException("First name cannot be empty"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            patientService.updatePatient(1L, updatedData);
        });
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test updating a patient with invalid date of birth.
     */
    @Test
    @DisplayName("Should fail to update patient with null date of birth")
    void testUpdatePatient_NullDateOfBirth() {
        // Arrange
        Patient updatedData = new Patient();
        updatedData.setFirstName("Updated");
        updatedData.setLastName("Updated");
        updatedData.setDateOfBirth(null);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new IllegalArgumentException("Date of birth cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            patientService.updatePatient(1L, updatedData);
        });
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    /**
     * Test updating a patient with negative ID.
     */
    @Test
    @DisplayName("Should throw exception when updating with invalid ID")
    void testUpdatePatient_InvalidID() {
        // Arrange
        Patient updatedData = new Patient();
        updatedData.setFirstName("Test");
        updatedData.setLastName("Test");

        when(patientRepository.findById(-1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            patientService.updatePatient(-1L, updatedData);
        });
        verify(patientRepository, times(1)).findById(-1L);
    }

    /**
     * Test creating a patient with future date of birth.
     */
    @Test
    @DisplayName("Should fail to create patient with future date of birth")
    void testCreatePatient_FutureDateOfBirth() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusYears(1);
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName("Future");
        invalidPatient.setLastName("Future");
        invalidPatient.setDateOfBirth(futureDate);

        
        assertThrows(InvalidDateException.class, () -> patientService.createPatient(invalidPatient));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    /**
     * Test updating a patient with future date of birth.
     */
    @Test
    @DisplayName("Should fail to update patient with future date of birth")
    void testUpdatePatient_FutureDateOfBirth() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusYears(2);
        Patient updatedData = new Patient();
        updatedData.setFirstName("FutureUpdate");
        updatedData.setLastName("FutureUpdate");
        updatedData.setDateOfBirth(futureDate);

        

        assertThrows(InvalidDateException.class, () -> patientService.updatePatient(1L, updatedData));
        verify(patientRepository, never()).save(any(Patient.class));
    }
}
