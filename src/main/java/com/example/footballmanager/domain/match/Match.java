package com.example.footballmanager.domain.match;

import com.example.footballmanager.domain.common.BaseEntity;
import com.example.footballmanager.domain.team.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    private int homeScore;

    private int awayScore;

    private LocalDateTime playedAt;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MatchEvent> events = new ArrayList<>();

    @Builder
    private Match(Team homeTeam, Team awayTeam, LocalDateTime playedAt, MatchStatus status) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.playedAt = playedAt;
        this.status = status;
    }

    public void start() {
        this.status = MatchStatus.IN_PROGRESS;
    }

    public void finish() {
        this.status = MatchStatus.FINISHED;
    }

    public void addHomeGoal() {
        this.homeScore++;
    }

    public void addAwayGoal() {
        this.awayScore++;
    }

    public void addEvent(MatchEvent event) {
        this.events.add(event);
    }
}
