package com.valorant.domain.jpa.repositories;

import com.valorant.models.Map;
import com.valorant.models.MapImpl;
import com.valorant.models.Match;
import com.valorant.models.MatchImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JpaMatchRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private JpaMatchRepository matchRepository;
    private JpaMapRepository mapRepository;

    @BeforeAll
    static void setUpBeforeClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("valorant-mysql");
        insertMapsIfNotExist();
    }

    @AfterAll
    static void tearDownAfterClass() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
        matchRepository = new JpaMatchRepository(entityManager);
        mapRepository = new JpaMapRepository(entityManager);
    }

    @AfterEach
    void tearDown() {
        if (entityManager != null) {
            EntityTransaction transaction = entityManager.getTransaction();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
        }
    }

    private static void insertMapsIfNotExist() {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Set<Map> maps = new JpaMapRepository(em).getAll();
        if (maps.isEmpty()) {
            // Insert some default maps
            Map map1 = new MapImpl();
            map1.setName("Lotus");
            map1.setType("Unranked");

            Map map2 = new MapImpl();
            map2.setName("Breeze");
            map2.setType("Competitive");

            Map map3 = new MapImpl();
            map3.setName("IceBox");
            map3.setType("Spike Rush");

            new JpaMapRepository(em).save(map1);
            new JpaMapRepository(em).save(map2);
            new JpaMapRepository(em).save(map3);
        }

        transaction.commit();
        em.close();
    }

    @Nested
    @DisplayName("Save Match Tests")
    class SaveMatchTests {

        @Test
        @DisplayName("Given a new match, when saved, then the match should have a valid ID")
        void saveNewMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Ensure map with ID 1 exists
            match.setOutcome("Victory");

            // Act
            matchRepository.save(match);

            // Assert
            assertTrue(match.getId() > 0, "Match ID should be greater than 0");
        }
    }

    @Nested
    @DisplayName("Update Match Tests")
    class UpdateMatchTests {

        @Test
        @DisplayName("Given an existing match with modified fields, when saved, then the match should be updated")
        void updateMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Ensure map with ID 1 exists
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

    @Nested
    @DisplayName("Delete Match Tests")
    class DeleteMatchTests {

        @Test
        @DisplayName("Given a match, when deleted, then the match should not exist in the repository")
        void deleteMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Ensure map with ID 1 exists
            match.setOutcome("Victory");

            matchRepository.save(match);

            // Act
            matchRepository.delete(match);

            // Assert
            assertNull(matchRepository.get(match.getId()), "Deleted match should be null");
        }

        @Test
        @DisplayName("Given a non-existent match, when deleted, then no exception should be thrown")
        void deleteNonExistentMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setId(9999);
            match.setOutcome("NonExistent");

            // Act & Assert
            assertDoesNotThrow(() -> matchRepository.delete(match), "Deleting a non-existent match should not throw an exception");
        }
    }

    @Nested
    @DisplayName("Retrieve Match Tests")
    class RetrieveMatchTests {

        @Test
        @DisplayName("Given a match ID, when retrieved, then the correct match should be returned")
        void getMatchTest() {
            // Arrange
            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Ensure map with ID 1 exists
            match.setOutcome("Victory");

            matchRepository.save(match);
            int matchId = match.getId();

            // Act
            Match retrievedMatch = matchRepository.get(matchId);

            // Assert
            assertNotNull(retrievedMatch, "Match should not be null");
            assertEquals(matchId, retrievedMatch.getId(), "Match ID should match the expected ID");
        }

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

        @Test
        @DisplayName("When all matches are retrieved, then the set should not be empty")
        void getAllMatchesTest() {
            // Arrange

            // Act
            Set<Match> matches = matchRepository.getAll();

            // Assert
            assertFalse(matches.isEmpty(), "The set of matches should not be empty");
        }

        @Test
        @DisplayName("Given a playedOn date, when retrieved, then the correct matches should be returned")
        void getMatchesByPlayedOnTest() {
            // Arrange
            LocalDateTime playedOn = LocalDateTime.of(2023, 5, 25, 15, 0);

            // Insert a match with the specified playedOn date
            Match match = new MatchImpl();
            match.setPlayedOn(playedOn);
            match.setMapId(1); // Ensure map with ID 1 exists
            match.setOutcome("Victory");
            matchRepository.save(match);

            // Act
            Set<Match> matches = matchRepository.getByPlayedOn(playedOn);

            // Assert
            assertNotNull(matches, "The set of matches should not be null");
            assertFalse(matches.isEmpty(), "The set of matches should not be empty");
        }

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
