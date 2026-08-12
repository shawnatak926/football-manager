package com.example.footballmanager.service.playermarket.dto;

import java.util.List;

public record EplMarketResponse(
        String league,
        int totalCount,
        String walletMode,
        List<EplMarketPlayerResponse> players
) {
}
