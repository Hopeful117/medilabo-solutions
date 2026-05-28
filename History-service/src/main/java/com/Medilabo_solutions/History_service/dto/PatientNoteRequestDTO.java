package com.Medilabo_solutions.History_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PatientNoteRequestDTO {
    @NotBlank(message = "Note content is required")
    private String content;

}
