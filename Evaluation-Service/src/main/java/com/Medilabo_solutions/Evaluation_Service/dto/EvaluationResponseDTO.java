package com.Medilabo_solutions.Evaluation_Service.dto;

import com.Medilabo_solutions.Evaluation_Service.helper.RiskLevels;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationResponseDTO {
    private long id;
    private RiskLevels riskLevel;

}
