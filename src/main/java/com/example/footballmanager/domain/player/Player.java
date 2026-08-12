package com.example.footballmanager.domain.player;

import com.example.footballmanager.domain.common.BaseEntity;
import com.example.footballmanager.domain.team.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PlayerPosition position;

    private int attack;

    private int defense;

    private int stamina;

    private int passing;

    private int finishing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    private Player(
            String name,
            PlayerPosition position,
            int attack,
            int defense,
            int stamina,
            int passing,
            int finishing
    ) {
        this.name = name;
        this.position = position;
        this.attack = attack;
        this.defense = defense;
        this.stamina = stamina;
        this.passing = passing;
        this.finishing = finishing;
    }

    public void assignTeam(Team team) {
        this.team = team;
    }
}
