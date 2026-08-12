package com.example.footballmanager.service.team.dto;

import com.example.footballmanager.domain.player.PlayerPosition;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomTeamPlayerRequest(
        @NotBlank String name,
        @NotNull PlayerPosition position,
        @NotNull @Min(1) @Max(100) Integer attack,
        @NotNull @Min(1) @Max(100) Integer defense,
        @NotNull @Min(1) @Max(100) Integer stamina,
        @NotNull @Min(1) @Max(100) Integer passing,
        @NotNull @Min(1) @Max(100) Integer finishing
) {
}
