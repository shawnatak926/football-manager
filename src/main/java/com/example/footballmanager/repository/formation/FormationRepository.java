package com.example.footballmanager.repository.formation;

import com.example.footballmanager.domain.formation.Formation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormationRepository extends JpaRepository<Formation, Long> {

    Optional<Formation> findByName(String name);
}
