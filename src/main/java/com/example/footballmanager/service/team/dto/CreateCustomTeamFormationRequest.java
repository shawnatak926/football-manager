package com.example.footballmanager.service.team.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomTeamFormationRequest(
        @NotBlank String name,
        @NotNull @Min(3) @Max(5) Integer defenders,
        @NotNull @Min(2) @Max(5) Integer midfielders,
        @NotNull @Min(1) @Max(3) Integer forwards,
        @NotNull @Min(0) @Max(30) Integer attackWeight,
        @NotNull @Min(0) @Max(30) Integer defenseWeight
) {
}
