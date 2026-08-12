package com.example.footballmanager.presentation.team;

import com.example.footballmanager.service.team.TeamQueryService;
import com.example.footballmanager.service.team.dto.CreateCustomTeamRequest;
import com.example.footballmanager.service.team.dto.CreateCustomTeamResponse;
import com.example.footballmanager.service.team.dto.EplTeamListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamQueryService teamQueryService;

    @GetMapping("/epl")
    public EplTeamListResponse getEplTeams() {
        return teamQueryService.getEplTeams();
    }

    @PostMapping("/epl/custom")
    public CreateCustomTeamResponse createCustomEplTeam(@Valid @RequestBody CreateCustomTeamRequest request) {
        return teamQueryService.createCustomEplTeam(request);
    }
}
