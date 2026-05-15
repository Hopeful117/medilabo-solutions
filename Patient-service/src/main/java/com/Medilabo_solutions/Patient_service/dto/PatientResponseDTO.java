package com.Medilabo_solutions.Patient_service.dto;

import lombok.Data;

import java.time.LocalDate;
/**
 * DTO for sending patient data in responses.
 */

@Data
public class PatientResponseDTO {
    private long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phoneNumber;
}
