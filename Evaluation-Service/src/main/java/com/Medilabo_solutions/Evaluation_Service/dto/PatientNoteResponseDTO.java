package com.Medilabo_solutions.Evaluation_Service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientNoteResponseDTO {
    private String id;
    private Long patientId;
    private String note;
    private LocalDateTime createdAt;
}
