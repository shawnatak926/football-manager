package com.example.footballmanager.service.bootstrap;

import com.example.footballmanager.domain.formation.Formation;
import com.example.footballmanager.domain.player.Player;
import com.example.footballmanager.domain.player.PlayerPosition;
import com.example.footballmanager.domain.tactic.Tactic;
import com.example.footballmanager.domain.tactic.TacticType;
import com.example.footballmanager.domain.team.Team;
import com.example.footballmanager.repository.formation.FormationRepository;
import com.example.footballmanager.repository.tactic.TacticRepository;
import com.example.footballmanager.repository.team.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitialDataService implements CommandLineRunner {

    private final FormationRepository formationRepository;
    private final TacticRepository tacticRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (teamRepository.count() > 0) {
            return;
        }

        Formation fourThreeThree = formationRepository.save(Formation.builder()
                .name("4-3-3")
                .defenders(4)
                .midfielders(3)
                .forwards(3)
                .attackWeight(14)
                .defenseWeight(8)
                .build());

        Formation fourFourTwo = formationRepository.save(Formation.builder()
                .name("4-4-2")
                .defenders(4)
                .midfielders(4)
                .forwards(2)
                .attackWeight(10)
                .defenseWeight(12)
                .build());

        Tactic highPress = tacticRepository.save(Tactic.builder()
                .name("High Press")
                .type(TacticType.HIGH_PRESS)
                .attackingBias(12)
                .defensiveBias(6)
                .pressingIntensity(15)
                .tempo(12)
                .build());

        Tactic counterAttack = tacticRepository.save(Tactic.builder()
                .name("Counter Attack")
                .type(TacticType.COUNTER_ATTACK)
                .attackingBias(10)
                .defensiveBias(10)
                .pressingIntensity(8)
                .tempo(14)
                .build());

        Team lions = Team.builder()
                .name("Seoul Lions")
                .morale(78)
                .formation(fourThreeThree)
                .tactic(highPress)
                .build();
        addPlayers(lions, List.of(
                player("Kim Min", PlayerPosition.GOALKEEPER, 40, 84, 75, 50, 42),
                player("Park Jun", PlayerPosition.DEFENDER, 48, 80, 79, 62, 45),
                player("Lee Hyun", PlayerPosition.DEFENDER, 44, 82, 76, 60, 43),
                player("Choi Gun", PlayerPosition.DEFENDER, 50, 78, 74, 63, 48),
                player("Han Sol", PlayerPosition.DEFENDER, 46, 79, 77, 61, 44),
                player("Jung Woo", PlayerPosition.MIDFIELDER, 68, 63, 81, 82, 65),
                player("Yoon Tae", PlayerPosition.MIDFIELDER, 66, 61, 80, 84, 64),
                player("Song Jin", PlayerPosition.MIDFIELDER, 70, 58, 78, 79, 68),
                player("Kang Ho", PlayerPosition.FORWARD, 82, 40, 77, 70, 84),
                player("Lim Kyu", PlayerPosition.FORWARD, 79, 42, 76, 68, 82),
                player("Shin Rok", PlayerPosition.FORWARD, 80, 39, 75, 69, 86)
        ));

        Team tigers = Team.builder()
                .name("Busan Tigers")
                .morale(74)
                .formation(fourFourTwo)
                .tactic(counterAttack)
                .build();
        addPlayers(tigers, List.of(
                player("Oh Jin", PlayerPosition.GOALKEEPER, 38, 83, 73, 49, 41),
                player("Seo Min", PlayerPosition.DEFENDER, 45, 81, 77, 57, 42),
                player("Baek Jun", PlayerPosition.DEFENDER, 47, 80, 75, 58, 44),
                player("Nam Hoon", PlayerPosition.DEFENDER, 49, 77, 76, 59, 47),
                player("Ahn Jae", PlayerPosition.DEFENDER, 43, 79, 74, 60, 43),
                player("Ryu Chan", PlayerPosition.MIDFIELDER, 67, 60, 79, 81, 63),
                player("Moon Seok", PlayerPosition.MIDFIELDER, 65, 62, 78, 78, 61),
                player("Bae Yul", PlayerPosition.MIDFIELDER, 63, 64, 80, 76, 60),
                player("Jang Hun", PlayerPosition.MIDFIELDER, 64, 63, 77, 74, 59),
                player("Koo Ram", PlayerPosition.FORWARD, 81, 41, 75, 67, 83),
                player("Hwang Bin", PlayerPosition.FORWARD, 78, 43, 74, 66, 81)
        ));

        teamRepository.save(lions);
        teamRepository.save(tigers);
    }

    private void addPlayers(Team team, List<Player> players) {
        players.forEach(team::addPlayer);
    }

    private Player player(String name, PlayerPosition position, int attack, int defense, int stamina, int passing, int finishing) {
        return Player.builder()
                .name(name)
                .position(position)
                .attack(attack)
                .defense(defense)
                .stamina(stamina)
                .passing(passing)
                .finishing(finishing)
                .build();
    }
}
