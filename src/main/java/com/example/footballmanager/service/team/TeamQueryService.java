package com.example.footballmanager.service.team;

import com.example.footballmanager.domain.formation.Formation;
import com.example.footballmanager.domain.player.Player;
import com.example.footballmanager.domain.player.PlayerPosition;
import com.example.footballmanager.domain.tactic.Tactic;
import com.example.footballmanager.domain.team.Team;
import com.example.footballmanager.repository.formation.FormationRepository;
import com.example.footballmanager.repository.tactic.TacticRepository;
import com.example.footballmanager.repository.team.TeamRepository;
import com.example.footballmanager.service.team.dto.CreateCustomTeamPlayerRequest;
import com.example.footballmanager.service.team.dto.CreateCustomTeamRequest;
import com.example.footballmanager.service.team.dto.CreateCustomTeamResponse;
import com.example.footballmanager.service.team.dto.EplTeamListResponse;
import com.example.footballmanager.service.team.dto.EplTeamResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class TeamQueryService {

    private static final List<EplTeamResponse> EPL_TEAMS_2026_2027 = List.of(
            new EplTeamResponse(1, "Arsenal", "ARS"),
            new EplTeamResponse(2, "Aston Villa", "AVL"),
            new EplTeamResponse(3, "Bournemouth", "BOU"),
            new EplTeamResponse(4, "Brentford", "BRE"),
            new EplTeamResponse(5, "Brighton & Hove Albion", "BHA"),
            new EplTeamResponse(6, "Chelsea", "CHE"),
            new EplTeamResponse(7, "Coventry City", "COV"),
            new EplTeamResponse(8, "Crystal Palace", "CRY"),
            new EplTeamResponse(9, "Everton", "EVE"),
            new EplTeamResponse(10, "Fulham", "FUL"),
            new EplTeamResponse(11, "Hull City", "HUL"),
            new EplTeamResponse(12, "Ipswich Town", "IPS"),
            new EplTeamResponse(13, "Leeds United", "LEE"),
            new EplTeamResponse(14, "Liverpool", "LIV"),
            new EplTeamResponse(15, "Manchester City", "MCI"),
            new EplTeamResponse(16, "Manchester United", "MUN"),
            new EplTeamResponse(17, "Newcastle United", "NEW"),
            new EplTeamResponse(18, "Nottingham Forest", "NFO"),
            new EplTeamResponse(19, "Sunderland", "SUN"),
            new EplTeamResponse(20, "Tottenham Hotspur", "TOT")
    );

    private final TeamRepository teamRepository;
    private final FormationRepository formationRepository;
    private final TacticRepository tacticRepository;

    public EplTeamListResponse getEplTeams() {
        return new EplTeamListResponse(
                "Premier League",
                "2026/27",
                EPL_TEAMS_2026_2027.size(),
                EPL_TEAMS_2026_2027
        );
    }

    @Transactional
    public CreateCustomTeamResponse createCustomEplTeam(CreateCustomTeamRequest request) {
        validateRequest(request);

        Formation formation = formationRepository.findByName(request.formation().name())
                .orElseGet(() -> formationRepository.save(Formation.builder()
                        .name(request.formation().name())
                        .defenders(request.formation().defenders())
                        .midfielders(request.formation().midfielders())
                        .forwards(request.formation().forwards())
                        .attackWeight(request.formation().attackWeight())
                        .defenseWeight(request.formation().defenseWeight())
                        .build()));

        Tactic tactic = tacticRepository.findByName(request.tactic().name())
                .orElseGet(() -> tacticRepository.save(Tactic.builder()
                        .name(request.tactic().name())
                        .type(request.tactic().type())
                        .attackingBias(request.tactic().attackingBias())
                        .defensiveBias(request.tactic().defensiveBias())
                        .pressingIntensity(request.tactic().pressingIntensity())
                        .tempo(request.tactic().tempo())
                        .build()));

        Team team = Team.builder()
                .name(request.teamName())
                .morale(request.morale())
                .formation(formation)
                .tactic(tactic)
                .build();

        request.players().stream()
                .map(this::toPlayer)
                .forEach(team::addPlayer);

        Team savedTeam = teamRepository.save(team);

        return new CreateCustomTeamResponse(
                savedTeam.getId(),
                savedTeam.getName(),
                savedTeam.getFormation().getName(),
                savedTeam.getTactic().getName(),
                savedTeam.getPlayers().size(),
                "Custom EPL team created successfully"
        );
    }

    private void validateRequest(CreateCustomTeamRequest request) {
        if (teamRepository.existsByName(request.teamName())) {
            throw new ResponseStatusException(BAD_REQUEST, "이미 존재하는 팀 이름입니다.");
        }

        int outfieldCount = request.formation().defenders() + request.formation().midfielders() + request.formation().forwards();
        if (outfieldCount != 10) {
            throw new ResponseStatusException(BAD_REQUEST, "포메이션 수비수, 미드필더, 공격수 합은 10이어야 합니다.");
        }

        long goalkeeperCount = request.players().stream()
                .filter(player -> player.position() == PlayerPosition.GOALKEEPER)
                .count();
        if (goalkeeperCount != 1) {
            throw new ResponseStatusException(BAD_REQUEST, "선수 명단에는 골키퍼가 정확히 1명 있어야 합니다.");
        }
    }

    private Player toPlayer(CreateCustomTeamPlayerRequest request) {
        return Player.builder()
                .name(request.name())
                .position(request.position())
                .attack(request.attack())
                .defense(request.defense())
                .stamina(request.stamina())
                .passing(request.passing())
                .finishing(request.finishing())
                .build();
    }
}
