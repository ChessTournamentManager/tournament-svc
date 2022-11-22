package com.chesstournamentmanager.tournamentsvc;

import com.chesstournamentmanager.tournamentsvc.config.TournamentData;
import com.chesstournamentmanager.tournamentsvc.repositories.TournamentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableCaching
@SpringBootApplication
@RestController
public class TournamentSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentSvcApplication.class, args);
	}
}
