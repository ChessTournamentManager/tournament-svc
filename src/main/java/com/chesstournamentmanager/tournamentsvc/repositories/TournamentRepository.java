package com.chesstournamentmanager.tournamentsvc.repositories;

import com.chesstournamentmanager.tournamentsvc.models.Tournament;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, UUID> {

}
