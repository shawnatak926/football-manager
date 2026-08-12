package com.example.footballmanager.domain.tactic;

import com.example.footballmanager.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tactic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TacticType type;

    private int attackingBias;

    private int defensiveBias;

    private int pressingIntensity;

    private int tempo;

    @Builder
    private Tactic(
            String name,
            TacticType type,
            int attackingBias,
            int defensiveBias,
            int pressingIntensity,
            int tempo
    ) {
        this.name = name;
        this.type = type;
        this.attackingBias = attackingBias;
        this.defensiveBias = defensiveBias;
        this.pressingIntensity = pressingIntensity;
        this.tempo = tempo;
    }
}
