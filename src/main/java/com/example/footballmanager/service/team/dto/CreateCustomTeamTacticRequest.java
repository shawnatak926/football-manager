package com.example.footballmanager.service.team.dto;

import com.example.footballmanager.domain.tactic.TacticType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomTeamTacticRequest(
        @NotBlank String name,
        @NotNull TacticType type,
        @NotNull @Min(0) @Max(30) Integer attackingBias,
        @NotNull @Min(0) @Max(30) Integer defensiveBias,
        @NotNull @Min(0) @Max(30) Integer pressingIntensity,
        @NotNull @Min(0) @Max(30) Integer tempo
) {
}
