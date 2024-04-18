package com.valorant.models;

// This interface represents a Player in the Valorant game.
public interface Player {
    // Get the unique identifier of the player.
    int getId();

    // Set the unique identifier of the player.
    void setId(int id);

    // Get the username of the player.
    String getUsername();

    // Set the username of the player.
    void setUsername(String username);

    // Get the display name of the player.
    String getDisplayName();

    // Set the display name of the player.
    void setDisplayName(String displayName);

    // Get the email address of the player.
    String getEmail();

    // Set the email address of the player.
    void setEmail(String email);

    // Get the region where the player is located.
    String getRegion();

    // Set the region where the player is located.
    void setRegion(String region);

    // Get the rank of the player.
    String getRank();

    // Set the rank of the player.
    void setRank(String rank);
}
