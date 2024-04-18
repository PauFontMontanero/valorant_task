// This class contains test cases for the MatchRepository class.
package com.valorant.file.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Test cases for MatchRepository class
class MatchRepositoryTest {
    private final String testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/test-matches.ser";
    private final String originalDataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/matches.ser";

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

    // Test case for retrieving matches by map ID
    @Test
    void getByMapId() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/get-matches.ser";

        // Create the repository using the data file path
        var repository = new MatchRepository(dataPath);

        // Create a new match instance
        var match = createMatch(LocalDateTime.now(), 1, "Win");

        // Save the match to the repository
        repository.save(match);

        // Attempt to retrieve the match by map ID from the repository and verify that it is not null
        assertNotNull(repository.getByMapId(1));
    }

    // Test case for updating a match
    @Test
    void updateMatch() {
        // Create a repository instance
        var repository = new MatchRepository(System.getProperty("user.dir") + "/src/main/resources/data/matches.ser");

        // Create a new match
        var match = createMatch(LocalDateTime.now(), 1, "Win");

        // Save the new match
        repository.save(match);

        // Assert that the match was saved successfully
        assertTrue(match.getId() > 0);
        assertNotNull(repository.get(match.getId()));

        // Update the outcome of the match
        match.setOutcome("Loss");
        repository.save(match);

        // Reload repository and verify updated match
        repository.load();
        var updatedMatch = repository.get(match.getId());
        assertNotNull(updatedMatch);
        assertEquals("Loss", updatedMatch.getOutcome());
    }

    // Test case for saving a new match
    @Test
    void saveNewMatch() {
        // Create a new repository instance
        var repository = new MatchRepository(System.getProperty("user.dir") + "/src/main/resources/data/matches.ser");

        // Create a new match
        var match = createMatch(LocalDateTime.now(), 1, "Win");

        // Save the new match
        repository.save(match);

        // Assert that the match was saved successfully
        assertTrue(match.getId() > 0);
        assertNotNull(repository.get(match.getId()));

        // Reload repository and verify saved match
        repository.load();
        assertNotNull(repository.get(match.getId()));
    }

    // Test case for deleting a match
    @Test
    void deleteMatch() throws IOException {
        // Define paths for temporary and test data files
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/test-delete-matches.ser";
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/delete-matches.ser";

        // Copy the test data file to the temporary location
        Files.copy(Path.of(testDataPath), Path.of(dataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a repository instance with the temporary data file
        var repository = new MatchRepository(dataPath);

        // Create a new match
        var match = createMatch(LocalDateTime.now(), 1, "Win");

        // Save the match to the repository
        repository.save(match);

        // Verify that the match exists before deletion
        assertNotNull(repository.get(match.getId()));

        // Delete the match
        repository.delete(match);

        // Verify that the match has been deleted
        assertNull(repository.get(match.getId()));
    }

    // Test case for retrieving a match
    @Test
    void getMatch() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/get-matches.ser";

        // Create the repository using the data file path
        var repository = new MatchRepository(dataPath);

        // Create a new match
        var match = createMatch(LocalDateTime.now(), 1, "Win");

        // Save the match to the repository
        repository.save(match);

        // Attempt to retrieve the match from the repository and verify that it is not null
        assertNotNull(repository.get(match.getId()));
    }

    // Test case for retrieving all matches
    @Test
    void getAllMatches() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/getAll-matches.ser";

        // Create the repository using the data file path
        var repository = new MatchRepository(dataPath);

        // Clear all matches from the repository
        repository.getAll().forEach(repository::delete);

        // Create multiple match instances
        var match1 = createMatch(LocalDateTime.now(), 1, "Win");
        var match2 = createMatch(LocalDateTime.now(), 2, "Loss");
        var match3 = createMatch(LocalDateTime.now(), 3, "Win");

        // Save the matches to the repository
        repository.save(match1);
        repository.save(match2);
        repository.save(match3);

        // Check if we can retrieve all matches from the repository
        assertEquals(3, repository.getAll().size());
    }

    // Test case for retrieving matches by playedOn date
    @Test
    void getByPlayedOn() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/match-tests/getByPlayedOn-matches.ser";

        // Create the repository using the data file path
        var repository = new MatchRepository(dataPath);

        // Create a base playedOn LocalDateTime
        LocalDateTime playedOn = LocalDateTime.of(2024, 4, 15, 12, 0);

        // Clear all matches from the repository
        repository.getAll().forEach(repository::delete);

        // Create two match instances with the same playedOn LocalDateTime
        var match1 = createMatch(playedOn, 1, "Win");
        var match2 = createMatch(playedOn, 2, "Loss");

        // Save the matches to the repository
        repository.save(match1);
        repository.save(match2);

        // Retrieve matches by playedOn date
        var matchesByPlayedOn = repository.getByPlayedOn(playedOn);

        // Verify that both matches are retrieved
        assertEquals(2, matchesByPlayedOn.size());
    }

    // Helper method to create a new Match instance
    private com.valorant.file.models.Match createMatch(LocalDateTime playedOn, int mapId, String outcome) {
        var match = new com.valorant.file.models.Match();
        match.setPlayedOn(playedOn);
        match.setMapId(mapId);
        match.setOutcome(outcome);
        return match;
    }
}
