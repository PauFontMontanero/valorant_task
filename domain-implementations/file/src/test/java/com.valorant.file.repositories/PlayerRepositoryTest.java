// This class contains test cases for the PlayerRepository class.
package com.valorant.file.repositories;

import com.valorant.models.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Test cases for PlayerRepository class
class PlayerRepositoryTest {
    private final String testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/test-players.ser";
    private final String originalDataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/players.ser";

    // Executed before each test method
    @BeforeEach
    void setUp() throws IOException {
        // Log file paths for verification
        System.out.println("Original Data Path: " + originalDataPath);
        System.out.println("Test Data Path: " + testDataPath);

        // Check if the original data file exists; if not, create it
        Path originalPath = Path.of(originalDataPath);
        if (!Files.exists(originalPath)) {
            System.out.println("Original data file does not exist. Creating...");
            Files.createDirectories(originalPath.getParent());
            Files.createFile(originalPath);
        }

        // Check if the test data file exists; if not, create it
        Path testPath = Path.of(testDataPath);
        if (!Files.exists(testPath)) {
            System.out.println("Test data file does not exist. Creating...");
            Files.createFile(testPath);
        }

        // Copy the original data file to the test directory
        System.out.println("Copying original data file to test directory...");
        Files.copy(originalPath, testPath, StandardCopyOption.REPLACE_EXISTING);

        // Verify if the test data file exists after creation
        System.out.println("Test data file exists: " + Files.exists(testPath));
    }

    // Executed after each test method
    @AfterEach
    void tearDown() throws IOException {
        // Delete the test data file after each test
        System.out.println("Deleting test data file...");
        Files.deleteIfExists(Path.of(testDataPath));
    }

    // Test case for saving a new player
    @Test
    void saveNewPlayer() {
        // Create a new repository instance
        var repository = new PlayerRepository(System.getProperty("user.dir") + "/src/main/resources/data/players.ser");

        // Create a new player
        var player = createPlayer("ItzSebiii", "Puppeteer#EUW", "bladesilviu526@gmail.com", "EUW", "Immortal2");

        // Save the new player
        repository.save(player);

        // Assert that the player was saved successfully
        assertTrue(player.getId() > 0);
        assertNotNull(repository.get(player.getId()).getUsername());

        // Reload repository and verify saved player
        repository.load();
        assertNotNull(repository.get(player.getId()));
        assertNotNull(repository.get(player.getId()).getUsername());
    }

    // Test case for deleting a player
    @Test
    void deletePlayer() throws IOException {
        // Define paths for temporary and test data files
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/test-delete-players.ser";
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/delete-players.ser";

        // Copy the test data file to the temporary location
        Files.copy(Path.of(testDataPath), Path.of(dataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a repository instance with the temporary data file
        var repository = new PlayerRepository(dataPath);

        // Define the username of the player to delete
        String username = "ItzSebiii";

        // Create a new player instance
        var playerToDelete = new com.valorant.file.models.Player();
        playerToDelete.setUsername(username);

        // Save the player to the repository
        repository.save(playerToDelete);

        // Verify that the player exists before deletion
        assertNotNull(repository.getByUsername(username));

        // Delete the player
        repository.delete(playerToDelete);

        // Verify that the player has been deleted
        assertNull(repository.getByUsername(username));
    }

    // Test case for retrieving a player
    @Test
    void getPlayer() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-players.ser";

        // Create the repository using the data file path
        var repository = new PlayerRepository(dataPath);

        // Create a new player instance
        var player = createPlayer("ItzSebiii", "Puppeteer#EUW", "bladesilviu526@gmail.com", "EUW", "Immortal2");

        // Save the player to the repository
        repository.save(player);

        // Attempt to retrieve the player from the repository and verify that it is not null
        assertNotNull(repository.getByUsername("ItzSebiii"));
    }

    // Test case for retrieving all players
    @Test
    void getAllPlayers() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/getAll-players.ser";

        // Create the repository using the data file path
        var repository = new PlayerRepository(dataPath);

        // Clear all players from the repository
        repository.getAll().forEach(repository::delete);

        // Create multiple player instances
        var player1 = createPlayer("player1", "Player One", "player1@example.com", "NA", "Platinum");
        var player2 = createPlayer("player2", "Player Two", "player2@example.com", "EU", "Gold");
        var player3 = createPlayer("player3", "Player Three", "player3@example.com", "APAC", "Diamond");

        // Save the players to the repository
        repository.save(player1);
        repository.save(player2);
        repository.save(player3);

        // Check if we can retrieve all players from the repository
        assertEquals(3, repository.getAll().size());
    }

    // Test case for retrieving email of a player
    @Test
    void getEmail() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-email.ser";

        // Create the repository using the data file path
        var repository = new PlayerRepository(dataPath);

        // Create a new player instance
        var player = createPlayer("player1", "Player One", "player1@example.com", "NA", "Platinum");

        // Save the player to the repository
        repository.save(player);

        // Attempt to retrieve the player's email from the repository and verify that it matches
        assertEquals("player1@example.com", repository.get(player.getId()).getEmail());
    }

    // Test case for retrieving rank of a player
    @Test
    void getRank() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-rank.ser";

        // Create the repository using the data file path
        var repository = new PlayerRepository(dataPath);

        // Create a new player instance
        var player = createPlayer("player2", "Player Two", "player2example.com", "EUW", "Radiant");

        // Save the player to the repository
        repository.save(player);

        // Attempt to retrieve the player's rank from the repository and verify that it matches
        assertEquals(player.getRank(), repository.get(player.getId()).getRank());
    }

    // Test case for retrieving players by region
    @Test
    void getByRegion() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-by-region.ser";

        // Create the repository using the data file path
        var repository = new PlayerRepository(dataPath);

        // Create some players with different regions
        var player1 = createPlayer("Max", "Maxito#fire", "maxdelacruz@gmail.com", "NA", "Gold");
        var player2 = createPlayer("Zoe", "ItzZoeee#2207", "zoe.balint@gmail.com", "EUW", "Silver");
        var player3 = createPlayer("Carles", "Carglass#Cambia", "carlgasscambia@gmail.com", "APAC", "Platinum");

        // Save the players to the repository
        repository.save(player1);
        repository.save(player2);
        repository.save(player3);

        // Count the expected number of players with the specified region
        long expectedPlayersInEU = repository.getAll().stream()
                .filter(player -> player.getRegion().equals("EU"))
                .count();
        long expectedPlayersInNA = repository.getAll().stream()
                .filter(player -> player.getRegion().equals("NA"))
                .count();

        // Attempt to retrieve players by region
        Set<Player> playersInEU = repository.getByRegion("EU");
        Set<Player> playersInNA = repository.getByRegion("NA");

        // Verify that the correct number of players are retrieved for each region
        assertEquals(expectedPlayersInEU, playersInEU.size());
        assertEquals(expectedPlayersInNA, playersInNA.size());
    }

    // Test case for retrieving players by display name
    @Test
    void getByDisplayName() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/player-tests/get-by-displayname.ser";

        // Create the repository using the data file path
        var repository = new PlayerRepository(dataPath);

        // Create some players with different display names
        var player1 = createPlayer("Max", "Maxito#fire", "maxdelacruz@gmail.com", "NA", "Gold");
        var player2 = createPlayer("Zoe", "ItzZoeee#2207", "zoe.balint@gmail.com", "EUW", "Silver");
        var player3 = createPlayer("Carles", "Carglass#Cambia", "carlgasscambia@gmail.com", "APAC", "Platinum");

        // Save the players to the repository
        repository.save(player1);
        repository.save(player2);
        repository.save(player3);

        // Count the expected number of players with the specified display name
        long expectedPlayersWithNameJohn = repository.getAll().stream()
                .filter(player -> player.getDisplayName().equals("Max"))
                .count();
        long expectedPlayersWithNameJane = repository.getAll().stream()
                .filter(player -> player.getDisplayName().equals("Zoe"))
                .count();
        long expectedPlayersWithNameAlice = repository.getAll().stream()
                .filter(player -> player.getDisplayName().equals("Carles"))
                .count();

        // Attempt to retrieve players by display name
        Set<Player> playersWithNameJohn = repository.getByDisplayName("Max");
        Set<Player> playersWithNameJane = repository.getByDisplayName("Zoe");
        Set<Player> playersWithNameAlice = repository.getByDisplayName("Carles");

        // Verify that the correct number of players are retrieved for each display name
        assertEquals(expectedPlayersWithNameJohn, playersWithNameJohn.size());
        assertEquals(expectedPlayersWithNameJane, playersWithNameJane.size());
        assertEquals(expectedPlayersWithNameAlice, playersWithNameAlice.size());
    }

    // Helper method to create a new Player instance
    private com.valorant.file.models.Player createPlayer(String username, String displayName, String email, String region, String rank) {
        var player = new com.valorant.file.models.Player();
        player.setUsername(username);
        player.setDisplayName(displayName);
        player.setEmail(email);
        player.setRegion(region);
        player.setRank(rank);
        return player;
    }
}
