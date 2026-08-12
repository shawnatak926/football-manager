package com.example.footballmanager.service.simulation.dto;

public record MatchEventResult(
        int minute,
        String teamName,
        String playerName,
        String eventType,
        String description
) {
}
