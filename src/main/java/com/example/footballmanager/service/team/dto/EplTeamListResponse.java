package com.example.footballmanager.service.team.dto;

import java.util.List;

public record EplTeamListResponse(
        String league,
        String season,
        int totalCount,
        List<EplTeamResponse> teams
) {
}
