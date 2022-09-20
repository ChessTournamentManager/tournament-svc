package com.chesstournamentmanager.tournamentsvc;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TournamentSvcApplicationTests {

	@Autowired
	private TournamentSvcApplication tournamentSvcApplication;

	@Test
	void contextLoads() {
	}

	@Test
	void sendsWelcomeMessage() {
		assertThat(tournamentSvcApplication.getMessage()).isEqualTo("Welcome to the tournament service.");
	}
}
