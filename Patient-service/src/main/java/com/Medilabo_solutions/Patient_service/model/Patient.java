package com.Medilabo_solutions.Patient_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a patient in the medical system.
 * Contains personal information such as name, date of birth
 */
@Entity
@Table(name = "patients")
@Getter
@Setter
@RequiredArgsConstructor

public class Patient {

    public enum Gender{
        M,
        F
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotBlank(message = "First name is required")
    private String firstName;


    @NotBlank(message = "Last name is required")
    private String lastName;


    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String phoneNumber;

    public Patient (String firstName,String lastName,LocalDate dateOfBirth,Gender gender){
        this.firstName=firstName;
        this.lastName=lastName;
        this.dateOfBirth=dateOfBirth;
        this.gender=gender;
    }


    }





