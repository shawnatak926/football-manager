package com.example.footballmanager.repository.player;

import com.example.footballmanager.domain.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
