package com.example.footballmanager.service.team.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CreateCustomTeamRequest(
        @NotBlank String teamName,
        @NotNull @Min(1) @Max(100) Integer morale,
        @NotNull @Valid CreateCustomTeamFormationRequest formation,
        @NotNull @Valid CreateCustomTeamTacticRequest tactic,
        @NotNull @Size(min = 11, max = 11) List<@Valid CreateCustomTeamPlayerRequest> players
) {
}
