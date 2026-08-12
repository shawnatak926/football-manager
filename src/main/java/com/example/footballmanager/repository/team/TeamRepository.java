package com.example.footballmanager.repository.team;

import com.example.footballmanager.domain.team.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @EntityGraph(attributePaths = {"formation", "tactic", "players"})
    Team findWithPlayersById(Long id);

    boolean existsByName(String name);
}
