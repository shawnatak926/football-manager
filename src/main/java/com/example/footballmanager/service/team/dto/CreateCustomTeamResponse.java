package com.example.footballmanager.service.team.dto;

public record CreateCustomTeamResponse(
        Long teamId,
        String teamName,
        String formationName,
        String tacticName,
        int playerCount,
        String message
) {
}
