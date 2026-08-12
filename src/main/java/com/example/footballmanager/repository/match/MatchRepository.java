package com.example.footballmanager.repository.match;

import com.example.footballmanager.domain.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
