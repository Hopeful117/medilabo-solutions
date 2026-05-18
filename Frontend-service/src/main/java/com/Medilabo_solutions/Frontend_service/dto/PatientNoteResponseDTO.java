package com.Medilabo_solutions.Frontend_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class PatientNoteResponseDTO {
    private String id;
    private Long patientId;
    private String note;
    private LocalDateTime createdAt;
}
