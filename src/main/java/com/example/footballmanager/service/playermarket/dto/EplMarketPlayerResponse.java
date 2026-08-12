package com.example.footballmanager.service.playermarket.dto;

public record EplMarketPlayerResponse(
        long playerId,
        String name,
        String club,
        String wikiTitle,
        String position,
        int overall,
        int attack,
        int defense,
        int stamina,
        int passing,
        int finishing,
        int price
) {
}
