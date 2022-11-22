package com.chesstournamentmanager.tournamentsvc.repositories;

import com.chesstournamentmanager.tournamentsvc.config.TournamentData;
import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface TournamentRepository extends CrudRepository<Tournament, UUID> {

}
