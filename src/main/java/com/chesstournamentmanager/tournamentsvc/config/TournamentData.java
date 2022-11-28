package com.chesstournamentmanager.tournamentsvc.config;

import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Component
public class TournamentData {

    @Bean
    public List<Tournament> GetTournaments() {
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(new Tournament(UUID.randomUUID(),"Judah", Tournament.Status.PLANNED, LocalDateTime.of(2022, Month.DECEMBER, 01, 13, 00, 00), 5, 20));
        return tournaments;
    }
}
