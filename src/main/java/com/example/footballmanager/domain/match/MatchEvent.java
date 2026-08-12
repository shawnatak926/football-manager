package com.example.footballmanager.domain.match;

import com.example.footballmanager.domain.common.BaseEntity;
import com.example.footballmanager.domain.player.Player;
import com.example.footballmanager.domain.team.Team;
import jakarta.persistence.Column;
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
public class MatchEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "event_minute")
    private int minute;

    @Enumerated(EnumType.STRING)
    private MatchEventType eventType;

    private String description;

    @Builder
    private MatchEvent(Match match, Team team, Player player, int minute, MatchEventType eventType, String description) {
        this.match = match;
        this.team = team;
        this.player = player;
        this.minute = minute;
        this.eventType = eventType;
        this.description = description;
    }
}
