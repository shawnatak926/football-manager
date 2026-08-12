package com.example.footballmanager.domain.team;

import com.example.footballmanager.domain.common.BaseEntity;
import com.example.footballmanager.domain.formation.Formation;
import com.example.footballmanager.domain.player.Player;
import com.example.footballmanager.domain.tactic.Tactic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int morale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id")
    private Formation formation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tactic_id")
    private Tactic tactic;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Player> players = new ArrayList<>();

    @Builder
    private Team(String name, int morale, Formation formation, Tactic tactic) {
        this.name = name;
        this.morale = morale;
        this.formation = formation;
        this.tactic = tactic;
    }

    public void assignFormation(Formation formation) {
        this.formation = formation;
    }

    public void assignTactic(Tactic tactic) {
        this.tactic = tactic;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.assignTeam(this);
    }
}
