package com.valorant.domain.jdbc.models;

import com.valorant.models.Match;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents a player in the Valorant game.
 * Implements the {@link com.valorant.models.Player} interface and Serializable for object serialization.
 */
public class Player implements com.valorant.models.Player, Serializable {
    private int id;             // Unique identifier for the player.
    private String username;    // Username of the player.
    private String displayName; // Display name of the player.
    private String email;       // Email address of the player.
    private String region;      // Region where the player is located.
    private String rank;        // Rank of the player in the game.

    /**
     * Getter method for retrieving the unique identifier of the player.
     *
     * @return the unique identifier of the player.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Setter method for setting the unique identifier of the player.
     *
     * @param id the unique identifier of the player.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for retrieving the username of the player.
     *
     * @return the username of the player.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for setting the username of the player.
     *
     * @param username the username of the player.
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for retrieving the display name of the player.
     *
     * @return the display name of the player.
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter method for setting the display name of the player.
     *
     * @param displayName the display name of the player.
     */
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter method for retrieving the email address of the player.
     *
     * @return the email address of the player.
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * Setter method for setting the email address of the player.
     *
     * @param email the email address of the player.
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter method for retrieving the region where the player is located.
     *
     * @return the region where the player is located.
     */
    @Override
    public String getRegion() {
        return region;
    }

    /**
     * Setter method for setting the region where the player is located.
     *
     * @param region the region where the player is located.
     */
    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Getter method for retrieving the rank of the player.
     *
     * @return the rank of the player.
     */
    @Override
    public String getRank() {
        return rank;
    }

    /**
     * Setter method for setting the rank of the player.
     *
     * @param rank the rank of the player.
     */
    @Override
    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public Set<Match> getMatches() {
        return Set.of();
    }

    @Override
    public void setMatches(Set<Match> matches) {

    }
}
