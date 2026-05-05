package com.Medilabo_solutions.Patient_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO for creating or updating a patient.
 */
public class PatientRequestDTO {
    @NotBlank(message = "First name is required")
    public String firstName;

    @NotBlank(message = "Last name is required")
    public String lastName;

    @NotNull(message = "Date of birth is required")
    public LocalDate dateOfBirth;

    @NotBlank(message= "Gender is required")
    public String gender;

    public String address;

    public String phoneNumber;
}
