package com.Medilabo_solutions.Frontend_service.helper;

import lombok.Getter;

@Getter
public enum RiskLevels {
    NONE("Aucun risque"),
    BORDERLINE("Risque limité"),
    IN_DANGER("Danger"),
    EARLY_ONSET("Apparition précoce");

    private final String description;

    RiskLevels(String description) {
        this.description = description;
    }

}