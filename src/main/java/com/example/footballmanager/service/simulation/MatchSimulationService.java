package com.example.footballmanager.service.simulation;

import com.example.footballmanager.domain.match.Match;
import com.example.footballmanager.domain.match.MatchEvent;
import com.example.footballmanager.domain.match.MatchEventType;
import com.example.footballmanager.domain.match.MatchStatus;
import com.example.footballmanager.domain.player.Player;
import com.example.footballmanager.domain.player.PlayerPosition;
import com.example.footballmanager.domain.team.Team;
import com.example.footballmanager.repository.match.MatchEventRepository;
import com.example.footballmanager.repository.match.MatchRepository;
import com.example.footballmanager.repository.team.TeamRepository;
import com.example.footballmanager.service.simulation.dto.MatchEventResult;
import com.example.footballmanager.service.simulation.dto.MatchSimulationRequest;
import com.example.footballmanager.service.simulation.dto.MatchSimulationResult;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchSimulationService {

    private static final List<Integer> EVENT_MINUTES = List.of(1, 8, 14, 23, 31, 39, 52, 64, 73, 81, 89, 90);

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;

    @Transactional
    public MatchSimulationResult simulate(MatchSimulationRequest request) {
        Team homeTeam = getTeam(request.homeTeamId());
        Team awayTeam = getTeam(request.awayTeamId());

        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .playedAt(LocalDateTime.now())
                .status(MatchStatus.SCHEDULED)
                .build();
        match.start();
        matchRepository.save(match);

        List<MatchEventResult> results = new ArrayList<>();
        registerEvent(match, null, null, 0, MatchEventType.KICK_OFF, "Kick off", results);

        TeamStrength homeStrength = evaluate(homeTeam);
        TeamStrength awayStrength = evaluate(awayTeam);

        for (int minute : EVENT_MINUTES) {
            processMinute(match, homeTeam, awayTeam, homeStrength, awayStrength, minute, results);
        }

        registerEvent(match, null, null, 90, MatchEventType.FULL_TIME, "Full time whistle", results);
        match.finish();

        return new MatchSimulationResult(
                match.getId(),
                homeTeam.getName(),
                awayTeam.getName(),
                match.getHomeScore(),
                match.getAwayScore(),
                buildSummary(match),
                results
        );
    }

    private Team getTeam(Long teamId) {
        Team team = teamRepository.findWithPlayersById(teamId);
        if (team == null) {
            throw new EntityNotFoundException("Team not found. id=" + teamId);
        }
        return team;
    }

    private void processMinute(
            Match match,
            Team homeTeam,
            Team awayTeam,
            TeamStrength homeStrength,
            TeamStrength awayStrength,
            int minute,
            List<MatchEventResult> results
    ) {
        double homeChance = calculateChance(homeStrength, awayStrength, true);
        double awayChance = calculateChance(awayStrength, homeStrength, false);
        double roll = ThreadLocalRandom.current().nextDouble();

        if (roll < homeChance) {
            resolveAttack(match, homeTeam, awayTeam, minute, true, results);
            return;
        }

        if (roll < homeChance + awayChance) {
            resolveAttack(match, awayTeam, homeTeam, minute, false, results);
            return;
        }

        Team foulingTeam = ThreadLocalRandom.current().nextBoolean() ? homeTeam : awayTeam;
        Player player = pickPlayer(foulingTeam, null);
        registerEvent(match, foulingTeam, player, minute, MatchEventType.FOUL, player.getName() + " commits a tactical foul", results);
    }

    private void resolveAttack(
            Match match,
            Team attackingTeam,
            Team defendingTeam,
            int minute,
            boolean isHomeTeam,
            List<MatchEventResult> results
    ) {
        Player creator = pickPlayer(attackingTeam, PlayerPosition.MIDFIELDER);
        registerEvent(match, attackingTeam, creator, minute, MatchEventType.CHANCE_CREATED, creator.getName() + " creates a clear chance", results);

        Player finisher = pickPlayer(attackingTeam, PlayerPosition.FORWARD);
        Player goalkeeper = pickPlayer(defendingTeam, PlayerPosition.GOALKEEPER);

        double finishingScore = finisher.getFinishing() + finisher.getAttack() * 0.35;
        double saveScore = goalkeeper.getDefense() + goalkeeper.getStamina() * 0.20;
        double goalProbability = Math.max(0.18, Math.min(0.82, 0.42 + ((finishingScore - saveScore) / 180.0)));

        if (ThreadLocalRandom.current().nextDouble() < goalProbability) {
            if (isHomeTeam) {
                match.addHomeGoal();
            } else {
                match.addAwayGoal();
            }
            registerEvent(match, attackingTeam, finisher, minute, MatchEventType.GOAL, finisher.getName() + " scores for " + attackingTeam.getName(), results);
            return;
        }

        registerEvent(match, defendingTeam, goalkeeper, minute, MatchEventType.SAVE, goalkeeper.getName() + " makes the save", results);
    }

    private double calculateChance(TeamStrength attackTeam, TeamStrength defenseTeam, boolean homeAdvantage) {
        double raw = 0.22 + ((attackTeam.attackPower() - defenseTeam.defensePower()) / 400.0)
                + ((attackTeam.controlPower() - defenseTeam.controlPower()) / 500.0);
        if (homeAdvantage) {
            raw += 0.04;
        }
        return Math.max(0.15, Math.min(0.48, raw));
    }

    private TeamStrength evaluate(Team team) {
        double attack = team.getPlayers().stream()
                .mapToDouble(player -> player.getAttack() + player.getFinishing() + player.getPassing() * 0.5)
                .average()
                .orElse(0.0);
        double defense = team.getPlayers().stream()
                .mapToDouble(player -> player.getDefense() + player.getStamina() * 0.4)
                .average()
                .orElse(0.0);
        double control = team.getPlayers().stream()
                .mapToDouble(player -> player.getPassing() + player.getStamina() * 0.3)
                .average()
                .orElse(0.0);

        attack += team.getFormation().getAttackWeight() + team.getTactic().getAttackingBias();
        defense += team.getFormation().getDefenseWeight() + team.getTactic().getDefensiveBias();
        control += team.getTactic().getTempo() + team.getTactic().getPressingIntensity() * 0.5 + team.getMorale() * 0.3;

        return new TeamStrength(attack, defense, control);
    }

    private Player pickPlayer(Team team, PlayerPosition preferredPosition) {
        return team.getPlayers().stream()
                .filter(player -> preferredPosition == null || player.getPosition() == preferredPosition)
                .max(Comparator.comparingInt(player -> player.getAttack() + player.getDefense() + player.getPassing() + player.getFinishing()))
                .orElseGet(() -> team.getPlayers().stream()
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Team has no players. teamId=" + team.getId())));
    }

    private void registerEvent(
            Match match,
            Team team,
            Player player,
            int minute,
            MatchEventType eventType,
            String description,
            List<MatchEventResult> results
    ) {
        MatchEvent event = MatchEvent.builder()
                .match(match)
                .team(team)
                .player(player)
                .minute(minute)
                .eventType(eventType)
                .description(description)
                .build();
        match.addEvent(event);
        matchEventRepository.save(event);

        results.add(new MatchEventResult(
                minute,
                team == null ? "-" : team.getName(),
                player == null ? "-" : player.getName(),
                eventType.name(),
                description
        ));
    }

    private String buildSummary(Match match) {
        return match.getHomeTeam().getName() + " " + match.getHomeScore() + " : "
                + match.getAwayScore() + " " + match.getAwayTeam().getName();
    }
}
