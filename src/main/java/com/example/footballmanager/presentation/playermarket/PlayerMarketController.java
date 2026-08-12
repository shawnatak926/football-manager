package com.example.footballmanager.presentation.playermarket;

import com.example.footballmanager.service.playermarket.PlayerMarketService;
import com.example.footballmanager.service.playermarket.dto.EplMarketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerMarketController {

    private final PlayerMarketService playerMarketService;

    @GetMapping("/epl-market")
    public EplMarketResponse getEplMarketPlayers() {
        return playerMarketService.getEplMarketPlayers();
    }
}
