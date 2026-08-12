package com.example.footballmanager.domain.formation;

import com.example.footballmanager.domain.common.BaseEntity;
import jakarta.persistence.Entity;
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
public class Formation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int defenders;

    private int midfielders;

    private int forwards;

    private int attackWeight;

    private int defenseWeight;

    @Builder
    private Formation(
            String name,
            int defenders,
            int midfielders,
            int forwards,
            int attackWeight,
            int defenseWeight
    ) {
        this.name = name;
        this.defenders = defenders;
        this.midfielders = midfielders;
        this.forwards = forwards;
        this.attackWeight = attackWeight;
        this.defenseWeight = defenseWeight;
    }
}
