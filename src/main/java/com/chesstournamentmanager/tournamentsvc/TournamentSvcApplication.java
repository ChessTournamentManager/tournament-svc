package com.chesstournamentmanager.tournamentsvc;

import com.chesstournamentmanager.tournamentsvc.config.TournamentData;
import com.chesstournamentmanager.tournamentsvc.repositories.TournamentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TournamentSvcApplication {
	private final TournamentRepository tournamentRepository;
	private final TournamentData tournamentData;;

	@Autowired
	public TournamentSvcApplication(TournamentRepository tournamentRepository, TournamentData tournamentData) {
		this.tournamentRepository = tournamentRepository;
		this.tournamentData = tournamentData;
	}

	public static void main(String[] args) {
		SpringApplication.run(TournamentSvcApplication.class, args);
	}
}
