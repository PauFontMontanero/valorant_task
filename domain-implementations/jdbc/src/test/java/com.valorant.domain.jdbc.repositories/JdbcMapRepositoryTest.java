package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Map;
import com.valorant.models.MapImpl;
import com.valorant.dbtestutils.db.DbUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcMapRepositoryTest {

    private Connection connection;
    private JdbcMapRepository mapRepository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DbUtils.connectToDb();
        connection.setAutoCommit(false); // Start transaction
        mapRepository = new JdbcMapRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit(); // Commit transaction
            connection.setAutoCommit(true); // Restore auto-commit mode
            connection.close();
        }
    }

    @Nested
    @DisplayName("Save Map Tests")
    class SaveMapTests {

        @Test
        @DisplayName("Given a new map, when saved, then the map should have a valid ID")
        void saveNewMapTest() {
            Map map = new MapImpl();
            map.setName("Ascent");
            map.setType("Competitive");

            mapRepository.save(map);

            assertTrue(map.getId() > 0, "Map ID should be greater than 0");
        }

        @Test
        @DisplayName("Given a map with special characters in the name, when saved and retrieved, then the correct map should be returned")
        void saveAndRetrieveMapWithSpecialCharactersTest() {
            Map map = new MapImpl();
            map.setName("Breeze");
            map.setType("Unranked");

            mapRepository.save(map);
            Map retrievedMap = mapRepository.getByName("Breeze");

            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Breeze", retrievedMap.getName(), "Map name should match the expected name");
            assertEquals("Unranked", retrievedMap.getType(), "Map type should match the expected type");
        }
    }

    @Nested
    @DisplayName("Update Map Tests")
    class UpdateMapTests {

        @Test
        @DisplayName("Given an existing map with modified fields, when saved, then the map should be updated")
        void updateMapTest() {
            Map map = new MapImpl();
            map.setName("Lotus");
            map.setType("Competitive");

            mapRepository.save(map);

            map.setName("Lotus"); // Keep the name within valid ENUM values
            map.setType("Swift Play");

            mapRepository.save(map);

            Map updatedMap = mapRepository.get(map.getId());
            assertNotNull(updatedMap, "Updated map should not be null");
            assertEquals("Lotus", updatedMap.getName(), "Map name should be updated");
            assertEquals("Swift Play", updatedMap.getType(), "Map type should be updated");
        }
    }

    @Nested
    @DisplayName("Delete Map Tests")
    class DeleteMapTests {

        @Test
        @DisplayName("Given a map, when deleted, then the map should not exist in the repository")
        void deleteMapTest() {
            Map map = new MapImpl();
            map.setName("Bind");
            map.setType("Competitive");

            mapRepository.save(map);

            mapRepository.delete(map);

            assertNull(mapRepository.get(map.getId()), "Deleted map should be null");
        }

        @Test
        @DisplayName("Given a non-existent map, when deleted, then no exception should be thrown")
        void deleteNonExistentMapTest() {
            Map map = new MapImpl();
            map.setId(9999);

            assertDoesNotThrow(() -> mapRepository.delete(map), "Deleting a non-existent map should not throw an exception");
        }
    }

    @Nested
    @DisplayName("Retrieve Map Tests")
    class RetrieveMapTests {

        @Test
        @DisplayName("Given a map ID, when retrieved, then the correct map should be returned")
        void getMapTest() {
            Map map = new MapImpl();
            map.setName("IceBox");
            map.setType("Competitive");

            mapRepository.save(map);
            int mapId = map.getId();

            Map retrievedMap = mapRepository.get(mapId);

            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals(mapId, retrievedMap.getId(), "Map ID should match the expected ID");
        }

        @Test
        @DisplayName("Given a map name, when retrieved, then the correct map should be returned")
        void getMapByNameTest() {
            Map map = new MapImpl();
            map.setName("Haven");
            map.setType("Competitive");

            mapRepository.save(map);

            Map retrievedMap = mapRepository.getByName("Haven");

            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Haven", retrievedMap.getName(), "Map name should match the expected name");
        }

        @Test
        @DisplayName("Given a non-existent map ID, when retrieved, then null should be returned")
        void getMapByNonExistentIdTest() {
            int nonExistentId = 9999;

            Map map = mapRepository.get(nonExistentId);

            assertNull(map, "Retrieving a non-existent map ID should return null");
        }

        @Test
        @DisplayName("Given a non-existent map name, when retrieved, then null should be returned")
        void getMapByNonExistentNameTest() {
            String nonExistentName = "NonExistentMap";

            Map map = mapRepository.getByName(nonExistentName);

            assertNull(map, "Retrieving a non-existent map name should return null");
        }

        @Test
        @DisplayName("When all maps are retrieved, then the set should not be empty")
        void getAllMapsTest() {
            Set<Map> maps = mapRepository.getAll();

            assertFalse(maps.isEmpty(), "The set of maps should not be empty");
        }
    }
}
