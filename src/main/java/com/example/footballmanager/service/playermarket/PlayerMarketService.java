package com.example.footballmanager.service.playermarket;

import com.example.footballmanager.service.playermarket.dto.EplMarketPlayerResponse;
import com.example.footballmanager.service.playermarket.dto.EplMarketResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlayerMarketService {

    private static final List<EplMarketPlayerResponse> EPL_MARKET_PLAYERS = List.of(
            new EplMarketPlayerResponse(1L, "Erling Haaland", "Manchester City", "Erling_Haaland", "FORWARD", 92, 97, 44, 87, 76, 96, 195),
            new EplMarketPlayerResponse(2L, "Bukayo Saka", "Arsenal", "Bukayo_Saka", "FORWARD", 90, 92, 52, 88, 87, 90, 168),
            new EplMarketPlayerResponse(3L, "Cole Palmer", "Chelsea", "Cole_Palmer", "MIDFIELDER", 89, 90, 50, 84, 91, 88, 162),
            new EplMarketPlayerResponse(4L, "Mohamed Salah", "Liverpool", "Mohamed_Salah", "FORWARD", 91, 94, 47, 85, 86, 93, 178),
            new EplMarketPlayerResponse(5L, "Bruno Fernandes", "Manchester United", "Bruno_Fernandes", "MIDFIELDER", 88, 86, 61, 84, 93, 84, 146),
            new EplMarketPlayerResponse(6L, "Alexander Isak", "Newcastle United", "Alexander_Isak", "FORWARD", 88, 91, 45, 83, 81, 91, 154),
            new EplMarketPlayerResponse(7L, "Son Heung-min", "Tottenham Hotspur", "Son_Heung-min", "FORWARD", 89, 92, 46, 84, 84, 92, 158),
            new EplMarketPlayerResponse(8L, "Martin Odegaard", "Arsenal", "Martin_Ødegaard", "MIDFIELDER", 88, 84, 63, 82, 92, 81, 149),
            new EplMarketPlayerResponse(9L, "Declan Rice", "Arsenal", "Declan_Rice", "MIDFIELDER", 89, 78, 88, 90, 86, 76, 152),
            new EplMarketPlayerResponse(10L, "Virgil van Dijk", "Liverpool", "Virgil_van_Dijk", "DEFENDER", 90, 66, 94, 80, 81, 60, 165),
            new EplMarketPlayerResponse(11L, "William Saliba", "Arsenal", "William_Saliba", "DEFENDER", 89, 63, 92, 84, 79, 55, 154),
            new EplMarketPlayerResponse(12L, "Ruben Dias", "Manchester City", "Rúben_Dias", "DEFENDER", 89, 61, 93, 79, 80, 53, 153),
            new EplMarketPlayerResponse(13L, "Trent Alexander-Arnold", "Liverpool", "Trent_Alexander-Arnold", "DEFENDER", 87, 79, 80, 82, 94, 72, 148),
            new EplMarketPlayerResponse(14L, "Alisson Becker", "Liverpool", "Alisson", "GOALKEEPER", 90, 34, 94, 83, 84, 28, 151),
            new EplMarketPlayerResponse(15L, "Ederson", "Manchester City", "Ederson_(footballer,_born_1993)", "GOALKEEPER", 89, 38, 92, 80, 89, 25, 145),
            new EplMarketPlayerResponse(16L, "Jordan Pickford", "Everton", "Jordan_Pickford", "GOALKEEPER", 85, 28, 88, 79, 78, 20, 101),
            new EplMarketPlayerResponse(17L, "Eberechi Eze", "Crystal Palace", "Eberechi_Eze", "MIDFIELDER", 86, 87, 53, 82, 86, 83, 132),
            new EplMarketPlayerResponse(18L, "Ollie Watkins", "Aston Villa", "Ollie_Watkins", "FORWARD", 87, 90, 44, 86, 78, 88, 139),
            new EplMarketPlayerResponse(19L, "Bryan Mbeumo", "Brentford", "Bryan_Mbeumo", "FORWARD", 85, 87, 49, 84, 81, 86, 126),
            new EplMarketPlayerResponse(20L, "Kaoru Mitoma", "Brighton & Hove Albion", "Kaoru_Mitoma", "FORWARD", 85, 88, 45, 83, 84, 82, 122),
            new EplMarketPlayerResponse(21L, "Joao Gomes", "Wolverhampton Wanderers", "João_Gomes_(footballer,_born_2001)", "MIDFIELDER", 83, 74, 84, 87, 77, 68, 96),
            new EplMarketPlayerResponse(22L, "Marc Guehi", "Crystal Palace", "Marc_Guehi", "DEFENDER", 85, 58, 88, 83, 76, 48, 121),
            new EplMarketPlayerResponse(23L, "Micky van de Ven", "Tottenham Hotspur", "Micky_van_de_Ven", "DEFENDER", 86, 61, 89, 88, 74, 46, 129),
            new EplMarketPlayerResponse(24L, "Dominik Szoboszlai", "Liverpool", "Dominik_Szoboszlai", "MIDFIELDER", 86, 84, 58, 85, 88, 82, 136),
            new EplMarketPlayerResponse(25L, "Phil Foden", "Manchester City", "Phil_Foden", "MIDFIELDER", 90, 91, 48, 84, 90, 89, 171)
    );

    public EplMarketResponse getEplMarketPlayers() {
        return new EplMarketResponse(
                "Premier League",
                EPL_MARKET_PLAYERS.size(),
                "UNLIMITED",
                EPL_MARKET_PLAYERS
        );
    }
}
