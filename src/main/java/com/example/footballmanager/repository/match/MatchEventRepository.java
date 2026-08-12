package com.example.footballmanager.repository.match;

import com.example.footballmanager.domain.match.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
}
