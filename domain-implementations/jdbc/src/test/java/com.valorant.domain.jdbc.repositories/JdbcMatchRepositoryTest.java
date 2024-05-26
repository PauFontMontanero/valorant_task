package com.valorant.domain.jdbc.repositories;

import com.valorant.dbtestutils.db.DbUtils;
import com.valorant.models.Map;
import com.valorant.models.MapImpl;
import com.valorant.models.Match;
import com.valorant.models.MatchImpl;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the JdbcMatchRepository.
 */
class JdbcMatchRepositoryTest {

    private static Connection connection;
    private static JdbcMatchRepository matchRepository;
    private static JdbcMapRepository mapRepository;

    /**
     * Sets up the database connection before all tests and inserts maps if no maps exist.
     *
     * @throws SQLException if a database access error occurs
     */
    @BeforeAll
    static void setUp() throws SQLException {
        connection = DbUtils.connectToDb();
        connection.setAutoCommit(false); // Start transaction
        matchRepository = new JdbcMatchRepository(connection);
        mapRepository = new JdbcMapRepository(connection);
        insertMapsIfNotExist();
    }

    /**
     * Commits the transaction and closes the database connection after all tests.
     *
     * @throws SQLException if a database access error occurs
     */
    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit(); // Commit transaction
            connection.setAutoCommit(true); // Restore auto-commit mode
            connection.close();
        }
    }

    /**
     * Inserts maps into the database if no maps exist.
     */
    private static void insertMapsIfNotExist() {
        Set<Map> maps = mapRepository.getAll();
        if (maps.isEmpty()) {
            // Insert some default maps
            Map map1 = new MapImpl();
            map1.setName("Lotus");
            map1.setType("Unranked");

            Map map2 = new MapImpl();
            map2.setName("Breeze");
            map2.setType("Competitive");

            Map map3 = new MapImpl();
            map2.setName("IceBox");
            map2.setType("Spike Rush");

            mapRepository.save(map1);
            mapRepository.save(map2);
        }
    }


    /**
     * Tests for match saving functionality.
     */
    @Nested
    @DisplayName("Save Match Tests")
    class SaveMatchTests {

        /**
         * Tests if a new match is correctly saved and assigned a valid ID.
         */
        @Test
        @DisplayName("Given a new match, when saved, then the match should have a valid ID")
        void saveNewMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Assuming map with ID 1 exists
            match.setOutcome("Victory");

            // Act
            matchRepository.save(match);

            // Assert
            assertTrue(match.getId() > 0, "Match ID should be greater than 0");
        }
    }

    /**
     * Tests for match updating functionality.
     */
    @Nested
    @DisplayName("Update Match Tests")
    class UpdateMatchTests {

        /**
         * Tests if an existing match with modified fields is correctly updated.
         */
        @Test
        @DisplayName("Given an existing match with modified fields, when saved, then the match should be updated")
        void updateMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Assuming map with ID 1 exists
            match.setOutcome("Victory");

            matchRepository.save(match);

            match.setOutcome("Defeat");

            // Act
            matchRepository.save(match);

            // Assert
            Match updatedMatch = matchRepository.get(match.getId());
            assertNotNull(updatedMatch, "Updated match should not be null");
            assertEquals("Defeat", updatedMatch.getOutcome(), "Match outcome should be updated");
        }
    }

    /**
     * Tests for match deleting functionality.
     */
    @Nested
    @DisplayName("Delete Match Tests")
    class DeleteMatchTests {

        /**
         * Tests if a match is correctly deleted.
         */
        @Test
        @DisplayName("Given a match, when deleted, then the match should not exist in the repository")
        void deleteMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Assuming map with ID 1 exists
            match.setOutcome("Victory");

            matchRepository.save(match);

            // Act
            matchRepository.delete(match);

            // Assert
            assertNull(matchRepository.get(match.getId()), "Deleted match should be null");
        }

        /**
         * Tests if deleting a non-existent match does not throw an exception.
         */
        @Test
        @DisplayName("Given a non-existent match, when deleted, then no exception should be thrown")
        void deleteNonExistentMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setId(9999);

            // Act & Assert
            assertDoesNotThrow(() -> matchRepository.delete(match), "Deleting a non-existent match should not throw an exception");
        }
    }

    /**
     * Tests for match retrieval functionality.
     */
    @Nested
    @DisplayName("Retrieve Match Tests")
    class RetrieveMatchTests {

        /**
         * Tests if a match is correctly retrieved by ID.
         */
        @Test
        @DisplayName("Given a match ID, when retrieved, then the correct match should be returned")
        void getMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Assuming map with ID 1 exists
            match.setOutcome("Victory");

            matchRepository.save(match);
            int matchId = match.getId();

            // Act
            Match retrievedMatch = matchRepository.get(matchId);

            // Assert
            assertNotNull(retrievedMatch, "Match should not be null");
            assertEquals(matchId, retrievedMatch.getId(), "Match ID should match the expected ID");
        }

        /**
         * Tests if retrieving a match by a non-existent ID returns null.
         */
        @Test
        @DisplayName("Given a non-existent match ID, when retrieved, then null should be returned")
        void getMatchByNonExistentIdTest() {
            // Arrange
            int nonExistentId = 9999;

            // Act
            Match match = matchRepository.get(nonExistentId);

            // Assert
            assertNull(match, "Retrieving a non-existent match ID should return null");
        }

        /**
         * Tests if all matches are correctly retrieved.
         */
        @Test
        @DisplayName("When all matches are retrieved, then the set should not be empty")
        void getAllMatchesTest() {
            // Arrange

            // Act
            Set<Match> matches = matchRepository.getAll();

            // Assert
            assertFalse(matches.isEmpty(), "The set of matches should not be empty");
        }

        /**
         * Tests if matches are correctly retrieved by playedOn date.
         */
        @Test
        @DisplayName("Given a playedOn date, when retrieved, then the correct matches should be returned")
        void getMatchesByPlayedOnTest() {
            // Arrange
            LocalDateTime playedOn = LocalDateTime.of(2023, 5, 25, 15, 0);

            // Insert a match with the specified playedOn date
            Match match = new MatchImpl();
            match.setPlayedOn(playedOn);
            match.setMapId(1); // Assuming map with ID 1 exists
            match.setOutcome("Victory");
            matchRepository.save(match);

            // Act
            Set<Match> matches = matchRepository.getByPlayedOn(playedOn);

            // Assert
            assertNotNull(matches, "The set of matches should not be null");
            assertFalse(matches.isEmpty(), "The set of matches should not be empty");
        }

        /**
         * Tests if matches are correctly retrieved by map ID.
         */
        @Test
        @DisplayName("Given a map ID, when retrieved, then the correct matches should be returned")
        void getMatchesByMapIdTest() {
            // Arrange
            int mapId = 1;

            // Insert a match with the specified map ID
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(mapId);
            match.setOutcome("Victory");
            matchRepository.save(match);

            // Act
            Set<Match> matches = matchRepository.getByMapId(mapId);

            // Assert
            assertNotNull(matches, "The set of matches should not be null");
            assertFalse(matches.isEmpty(), "The set of matches should not be empty");
        }
    }
}