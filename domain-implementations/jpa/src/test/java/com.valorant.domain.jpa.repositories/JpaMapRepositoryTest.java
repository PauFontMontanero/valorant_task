package com.valorant.domain.jpa.repositories;

import com.valorant.models.Map;
import com.valorant.models.MapImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JpaMapRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private JpaMapRepository mapRepository;

    @BeforeAll
    static void setUpBeforeClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("valorant-mysql");
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
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
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

    @Nested
    @DisplayName("Save Map Tests")
    class SaveMapTests {

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

        @Test
        @DisplayName("Given a map with special characters in the name, when saved and retrieved, then the correct map should be returned")
        void saveAndRetrieveMapWithSpecialCharactersTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Breeze@");
            map.setType("Unranked");

            // Act
            mapRepository.save(map);
            Map retrievedMap = mapRepository.getByName("Breeze@");

            // Assert
            assertNotNull(retrievedMap, "Map should not be null");
            assertEquals("Breeze@", retrievedMap.getName(), "Map name should match the expected name");
            assertEquals("Unranked", retrievedMap.getType(), "Map type should match the expected type");
        }
    }

    @Nested
    @DisplayName("Update Map Tests")
    class UpdateMapTests {

        @Test
        @DisplayName("Given an existing map with modified fields, when saved, then the map should be updated")
        void updateMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Lotus");
            map.setType("Competitive");

            mapRepository.save(map);
            int mapId = map.getId();

            // Retrieve the map from the repository to ensure it was saved correctly
            map = mapRepository.get(mapId);
            assertNotNull(map, "Map should not be null");

            // Update the map's fields
            map.setName("Updated Lotus");
            map.setType("Swift Play");

            // Act: Save the updated map
            mapRepository.save(map);

            // Assert
            Map updatedMap = mapRepository.get(mapId);
            assertNotNull(updatedMap, "Updated map should not be null");
            assertEquals(mapId, updatedMap.getId(), "Map ID should remain unchanged");
            assertEquals("Updated Lotus", updatedMap.getName(), "Map name should be updated");
            assertEquals("Swift Play", updatedMap.getType(), "Map type should be updated");
        }
    }

    @Nested
    @DisplayName("Delete Map Tests")
    class DeleteMapTests {

        @Test
        @DisplayName("Given a map, when deleted, then the map should not exist in the repository")
        void deleteMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setName("Bind");
            map.setType("Competitive");

            mapRepository.save(map);
            int mapId = map.getId();

            // Act
            mapRepository.delete(map);

            // Assert
            assertNull(mapRepository.get(mapId), "Deleted map should be null");
        }

        @Test
        @DisplayName("Given a non-existent map, when deleted, then no exception should be thrown")
        void deleteNonExistentMapTest() {
            // Arrange
            Map map = new MapImpl();
            map.setId(9999);
            map.setName("NonExistent");
            map.setType("NonExistent");

            // Act & Assert
            assertDoesNotThrow(() -> mapRepository.delete(map), "Deleting a non-existent map should not throw an exception");
        }
    }

    @Nested
    @DisplayName("Retrieve Map Tests")
    class RetrieveMapTests {

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

        @Test
        @DisplayName("When all maps are retrieved, then the set should not be empty")
        void getAllMapsTest() {
            // Arrange
            Map map1 = new MapImpl();
            map1.setName("Ascent");
            map1.setType("Competitive");

            Map map2 = new MapImpl();
            map2.setName("Breeze");
            map2.setType("Unranked");

            mapRepository.save(map1);
            mapRepository.save(map2);

            // Act
            Set<Map> maps = mapRepository.getAll();

            // Assert
            assertFalse(maps.isEmpty(), "The set of maps should not be empty");
        }
    }
}
