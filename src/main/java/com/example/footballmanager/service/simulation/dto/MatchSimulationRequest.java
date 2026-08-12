package com.example.footballmanager.service.simulation.dto;

import jakarta.validation.constraints.NotNull;

public record MatchSimulationRequest(
        @NotNull Long homeTeamId,
        @NotNull Long awayTeamId
) {
}
