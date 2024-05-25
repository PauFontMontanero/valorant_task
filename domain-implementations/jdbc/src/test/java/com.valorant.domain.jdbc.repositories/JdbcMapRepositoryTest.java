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
        mapRepository = new JdbcMapRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
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

            Map retrievedMap = mapRepository.get(map.getId());
            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Ascent", retrievedMap.getName(), "Map name should match the expected name");
            assertEquals("Competitive", retrievedMap.getType(), "Map type should match the expected type");
        }

        @Test
        @DisplayName("Given a map with special characters in the name, when saved and retrieved, then the correct map should be returned")
        void saveAndRetrieveMapWithSpecialCharactersTest() {
            Map map = new MapImpl();
            map.setName("Fracture@Valorant");
            map.setType("Unranked");

            mapRepository.save(map);
            Map retrievedMap = mapRepository.getByName("Fracture@Valorant");

            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Fracture@Valorant", retrievedMap.getName(), "Map name should match the expected name");
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
            map.setName("Breeze");
            map.setType("Competitive");

            mapRepository.save(map);

            map.setName("Updated Breeze");
            mapRepository.save(map);

            Map updatedMap = mapRepository.get(map.getId());
            assertNotNull(updatedMap, "Updated map should not be null");
            assertEquals("Updated Breeze", updatedMap.getName(), "Map name should be updated");
            assertEquals("Competitive", updatedMap.getType(), "Map type should be updated");
        }
    }

    @Nested
    @DisplayName("Delete Map Tests")
    class DeleteMapTests {

        @Test
        @DisplayName("Given a map, when deleted, then the map should not exist in the repository")
        void deleteMapTest() {
            Map map = new MapImpl();
            map.setName("Ascent");
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
            map.setName("Ascent");
            map.setType("Competitive");

            mapRepository.save(map);

            Map retrievedMap = mapRepository.get(map.getId());
            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Ascent", retrievedMap.getName(), "Map name should match the expected name");
            assertEquals("Competitive", retrievedMap.getType(), "Map type should match the expected type");
        }

        @Test
        @DisplayName("Given a map name, when retrieved, then the correct map should be returned")
        void getMapByNameTest() {
            Map map = new MapImpl();
            map.setName("Ascent");
            map.setType("Competitive");

            mapRepository.save(map);

            Map retrievedMap = mapRepository.getByName("Ascent");
            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Ascent", retrievedMap.getName(), "Map name should match the expected name");
            assertEquals("Competitive", retrievedMap.getType(), "Map type should match the expected type");
        }
    }
}