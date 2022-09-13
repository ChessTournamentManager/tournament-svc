package com.chesstournamentmanager.tournamentsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TournamentSvcApplication {

	@GetMapping("/message")
	public String getMessage() {
		return "Welcome to the tournament service.";
	}

	public static void main(String[] args) {
		SpringApplication.run(TournamentSvcApplication.class, args);
	}

}
