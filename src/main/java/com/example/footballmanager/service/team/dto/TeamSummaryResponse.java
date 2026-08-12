package com.example.footballmanager.service.team.dto;

public record TeamSummaryResponse(
        Long teamId,
        String teamName,
        String formationName,
        String tacticName,
        int morale,
        int playerCount
) {
}
