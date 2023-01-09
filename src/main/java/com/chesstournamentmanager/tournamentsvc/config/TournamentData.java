package com.chesstournamentmanager.tournamentsvc.config;

import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class TournamentData {

    @Bean
    public List<Tournament> getTournaments() {
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(new Tournament(UUID.randomUUID(),"Amazing tournament!", Tournament.Status.PLANNED, LocalDateTime.of(2025, Month.JANUARY, 1, 13, 0, 0), 5, 20));
        tournaments.add(new Tournament(UUID.randomUUID(),"KNSB Toernooi 2023", Tournament.Status.PLANNED, LocalDateTime.of(2027, Month.DECEMBER, 28, 14, 0, 0), 8, 60));
        tournaments.add(new Tournament(UUID.randomUUID(),"Labooft", Tournament.Status.PLANNED, LocalDateTime.of(2026, Month.JUNE, 5, 4, 0, 0), 3, 3));
        return tournaments;
    }
}
