package com.valorant.repositories;

import com.valorant.models.Match;

import java.time.LocalDateTime;
import java.util.Set;

// This interface defines operations to be performed on Match entities in the repository.
public interface MatchRepository extends Repository<Integer, Match> {

    // Save a new or existing match to the repository.
    @Override
    void save(Match model);

    // Delete the specified match from the repository.
    @Override
    void delete(Match model);

    // Retrieve a match by its unique identifier.
    @Override
    Match get(Integer id);

    // Retrieve all matches stored in the repository.
    @Override
    Set<Match> getAll();

    // Retrieve matches played on the specified date and time.
    Set<Match> getByPlayedOn(LocalDateTime playedOn);

    // Retrieve matches played on the map with the specified ID.
    Set<Match> getByMapId(int mapId);
}
