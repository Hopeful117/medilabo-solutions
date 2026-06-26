package com.Medilabo_solutions.Patient_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating or updating a patient.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message= "Gender is required")
    private String gender;

    private String address;

    private String phoneNumber;

   public PatientRequestDTO (String firstName,String lastName,LocalDate dateOfBirth,String gender){
       this.firstName = firstName;
       this.lastName = lastName;
       this.dateOfBirth = dateOfBirth;
       this.gender = gender;

    }


}
