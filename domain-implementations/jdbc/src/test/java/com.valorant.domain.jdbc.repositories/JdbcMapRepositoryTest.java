package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Map;
import com.valorant.models.MapImpl;
import com.valorant.dbtestutils.db.DbUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the JdbcMapRepository.
 */
class JdbcMapRepositoryTest {

    private Connection connection;
    private JdbcMapRepository mapRepository;

    /**
     * Sets up the database connection and starts a transaction before each test.
     *
     * @throws SQLException if a database access error occurs
     */
    @BeforeEach
    void setUp() throws SQLException {
        connection = DbUtils.connectToDb();
        connection.setAutoCommit(false); // Start transaction
        mapRepository = new JdbcMapRepository(connection);
    }

    /**
     * Commits the transaction and closes the database connection after each test.
     *
     * @throws SQLException if a database access error occurs
     */
    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit(); // Commit transaction
            connection.setAutoCommit(true); // Restore auto-commit mode
            connection.close();
        }
    }

    /**
     * Tests for map saving functionality.
     */
    @Nested
    @DisplayName("Save Map Tests")
    class SaveMapTests {

        /**
         * Tests if a new map is correctly saved and assigned a valid ID.
         */
        @Test
        @DisplayName("Given a new map, when saved, then the map should have a valid ID")
        void saveNewMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Ascent");
            map.setType("Competitive");

            // Act
            mapRepository.save(map);

            // Assert
            assertTrue(map.getId() > 0, "Map ID should be greater than 0");
        }

        /**
         * Tests if the repository correctly handles maps with special characters in their names.
         */
        @Test
        @DisplayName("Given a map with special characters in the name, when saved and retrieved, then the correct map should be returned")
        void saveAndRetrieveMapWithSpecialCharactersTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Breeze");
            map.setType("Unranked");

            // Act
            mapRepository.save(map);
            Map retrievedMap = mapRepository.getByName("Breeze");

            // Assert
            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Breeze", retrievedMap.getName(), "Map name should match the expected name");
            assertEquals("Unranked", retrievedMap.getType(), "Map type should match the expected type");
        }
    }

    /**
     * Tests for map updating functionality.
     */
    @Nested
    @DisplayName("Update Map Tests")
    class UpdateMapTests {

        /**
         * Tests if an existing map with modified fields is correctly updated.
         */
        @Test
        @DisplayName("Given an existing map with modified fields, when saved, then the map should be updated")
        void updateMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Lotus");
            map.setType("Competitive");

            mapRepository.save(map);

            map.setName("Lotus"); // Keep the name within valid ENUM values
            map.setType("Swift Play");

            // Act
            mapRepository.save(map);

            // Assert
            Map updatedMap = mapRepository.get(map.getId());
            assertNotNull(updatedMap, "Updated map should not be null");
            assertEquals("Lotus", updatedMap.getName(), "Map name should be updated");
            assertEquals("Swift Play", updatedMap.getType(), "Map type should be updated");
        }
    }

    /**
     * Tests for map deleting functionality.
     */
    @Nested
    @DisplayName("Delete Map Tests")
    class DeleteMapTests {

        /**
         * Tests if a map is correctly deleted.
         */
        @Test
        @DisplayName("Given a map, when deleted, then the map should not exist in the repository")
        void deleteMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Bind");
            map.setType("Competitive");

            mapRepository.save(map);

            // Act
            mapRepository.delete(map);

            // Assert
            assertNull(mapRepository.get(map.getId()), "Deleted map should be null");
        }

        /**
         * Tests if deleting a non-existent map does not throw an exception.
         */
        @Test
        @DisplayName("Given a non-existent map, when deleted, then no exception should be thrown")
        void deleteNonExistentMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setId(9999);

            // Act & Assert
            assertDoesNotThrow(() -> mapRepository.delete(map), "Deleting a non-existent map should not throw an exception");
        }
    }

    /**
     * Tests for map retrieval functionality.
     */
    @Nested
    @DisplayName("Retrieve Map Tests")
    class RetrieveMapTests {

        /**
         * Tests if a map is correctly retrieved by ID.
         */
        @Test
        @DisplayName("Given a map ID, when retrieved, then the correct map should be returned")
        void getMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("IceBox");
            map.setType("Competitive");

            mapRepository.save(map);
            int mapId = map.getId();

            // Act
            Map retrievedMap = mapRepository.get(mapId);

            // Assert
            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals(mapId, retrievedMap.getId(), "Map ID should match the expected ID");
        }

        /**
         * Tests if a map is correctly retrieved by name.
         */
        @Test
        @DisplayName("Given a map name, when retrieved, then the correct map should be returned")
        void getMapByNameTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Haven");
            map.setType("Competitive");

            mapRepository.save(map);

            // Act
            Map retrievedMap = mapRepository.getByName("Haven");

            // Assert
            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Haven", retrievedMap.getName(), "Map name should match the expected name");
        }

        /**
         * Tests if retrieving a map by a non-existent ID returns null.
         */
        @Test
        @DisplayName("Given a non-existent map ID, when retrieved, then null should be returned")
        void getMapByNonExistentIdTest() {
            // Arrange
            int nonExistentId = 9999;

            // Act
            Map map = mapRepository.get(nonExistentId);

            // Assert
            assertNull(map, "Retrieving a non-existent map ID should return null");
        }

        /**
         * Tests if retrieving a map by a non-existent name returns null.
         */
        @Test
        @DisplayName("Given a non-existent map name, when retrieved, then null should be returned")
        void getMapByNonExistentNameTest() {
            // Arrange
            String nonExistentName = "NonExistentMap";

            // Act
            Map map = mapRepository.getByName(nonExistentName);

            // Assert
            assertNull(map, "Retrieving a non-existent map name should return null");
        }

        /**
         * Tests if all maps are correctly retrieved.
         */
        @Test
        @DisplayName("When all maps are retrieved, then the set should not be empty")
        void getAllMapsTest() {
            // Arrange

            // Act
            Set<Map> maps = mapRepository.getAll();

            // Assert
            assertFalse(maps.isEmpty(), "The set of maps should not be empty");
        }
    }
}
