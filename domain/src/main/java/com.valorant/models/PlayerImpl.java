package com.valorant.models;

/**
 * Concrete implementation of the {@link Player} interface representing a player in the Valorant game.
 */
public class PlayerImpl implements Player {

    private int id;
    private String username;
    private String displayName;
    private String email;
    private String region;
    private String rank;

    /**
     * Get the unique identifier of the player.
     *
     * @return the unique identifier of the player.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Set the unique identifier of the player.
     *
     * @param id the unique identifier of the player.
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the username of the player.
     *
     * @return the username of the player.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the player.
     *
     * @param username the username of the player.
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the display name of the player.
     *
     * @return the display name of the player.
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set the display name of the player.
     *
     * @param displayName the display name of the player.
     */
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the email address of the player.
     *
     * @return the email address of the player.
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address of the player.
     *
     * @param email the email address of the player.
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the region where the player is located.
     *
     * @return the region where the player is located.
     */
    @Override
    public String getRegion() {
        return region;
    }

    /**
     * Set the region where the player is located.
     *
     * @param region the region where the player is located.
     */
    @Override
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Get the rank of the player.
     *
     * @return the rank of the player.
     */
    @Override
    public String getRank() {
        return rank;
    }

    /**
     * Set the rank of the player.
     *
     * @param rank the rank of the player.
     */
    @Override
    public void setRank(String rank) {
        this.rank = rank;
    }
}
