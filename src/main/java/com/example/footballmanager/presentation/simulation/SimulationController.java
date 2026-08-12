package com.example.footballmanager.presentation.simulation;

import com.example.footballmanager.service.simulation.MatchSimulationService;
import com.example.footballmanager.service.simulation.dto.MatchSimulationRequest;
import com.example.footballmanager.service.simulation.dto.MatchSimulationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class SimulationController {

    private final MatchSimulationService matchSimulationService;

    @PostMapping("/simulate")
    public MatchSimulationResult simulate(@Valid @RequestBody MatchSimulationRequest request) {
        return matchSimulationService.simulate(request);
    }
}
