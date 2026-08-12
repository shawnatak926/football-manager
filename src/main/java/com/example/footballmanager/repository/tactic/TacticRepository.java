package com.example.footballmanager.repository.tactic;

import com.example.footballmanager.domain.tactic.Tactic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacticRepository extends JpaRepository<Tactic, Long> {

    Optional<Tactic> findByName(String name);
}
