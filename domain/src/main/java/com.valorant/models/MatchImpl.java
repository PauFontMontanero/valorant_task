package com.valorant.models;

import java.time.LocalDateTime;

/**
 * Concrete implementation of the {@link Match} interface representing a Match in the Valorant game.
 */
public class MatchImpl implements Match {

    private int id;
    private LocalDateTime playedOn;
    private int mapId;
    private String outcome;

    /**
     * Get the unique identifier of the match.
     *
     * @return the unique identifier of the match.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the unique identifier of the match.
     *
     * @param id the unique identifier of the match.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the timestamp when the match was played.
     *
     * @return the timestamp when the match was played.
     */
    @Override
    public LocalDateTime getPlayedOn() {
        return playedOn;
    }

    /**
     * Set the timestamp when the match was played.
     *
     * @param playedOn the timestamp when the match was played.
     */
    @Override
    public void setPlayedOn(LocalDateTime playedOn) {
        this.playedOn = playedOn;
    }

    /**
     * Get the ID of the map associated with the match.
     *
     * @return the ID of the map associated with the match.
     */
    @Override
    public int getMapId() {
        return mapId;
    }

    /**
     * Set the ID of the map associated with the match.
     *
     * @param mapId the ID of the map associated with the match.
     */
    @Override
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    /**
     * Get the outcome of the match (e.g., Victory, Defeat, Draw).
     *
     * @return the outcome of the match.
     */
    @Override
    public String getOutcome() {
        return outcome;
    }

    /**
     * Set the outcome of the match.
     *
     * @param outcome the outcome of the match.
     */
    @Override
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
