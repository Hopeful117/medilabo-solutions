package com.Medilabo_solutions.Patient_service.dto;

import java.time.LocalDate;
/**
 * DTO for sending patient data in responses.
 */
public class PatientResponseDTO {
    public long id;
    public String firstName;
    public String lastName;
    public LocalDate dateOfBirth;
    public String gender;
    public String address;
    public String phoneNumber;
}
