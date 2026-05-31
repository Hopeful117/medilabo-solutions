package com.Medilabo_solutions.History_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientNoteRequestDTO {
    @NotBlank(message = "Note content is required")
    private String content;

}
