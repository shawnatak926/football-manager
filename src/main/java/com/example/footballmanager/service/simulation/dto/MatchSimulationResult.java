package com.example.footballmanager.service.simulation.dto;

import java.util.List;

public record MatchSimulationResult(
        Long matchId,
        String homeTeamName,
        String awayTeamName,
        int homeScore,
        int awayScore,
        String summary,
        List<MatchEventResult> events
) {
}
