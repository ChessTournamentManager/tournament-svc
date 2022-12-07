package com.chesstournamentmanager.tournamentsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TournamentSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentSvcApplication.class, args);
	}
}
