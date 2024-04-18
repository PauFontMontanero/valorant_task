// This class contains test cases for the MapRepository class.
package com.valorant.file.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

// Test cases for MapRepository class
class MapRepositoryTest {
    private final String testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/test-maps.ser";
    private final String originalDataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/maps.ser";

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

    // Test case for updating a map
    @Test
    void updateMap() throws IOException {
        // Copy the update data file to the test directory
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/update-maps.ser";
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/test-update-maps.ser";
        Files.copy(Path.of(dataPath), Path.of(testDataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a new repository instance with the test data
        var repository = new MapRepository(testDataPath);

        // Create and save a new map
        var ascent = createMap("Icebox");
        repository.save(ascent);

        // Update the map's name
        ascent.setName("Ascent");
        repository.save(ascent);

        // Retrieve the updated map
        var updatedMap = repository.get(ascent.getId());

        // Assert that the map was updated successfully
        assertEquals("Ascent", updatedMap.getName());
    }

    // Test case for saving a new map
    @Test
    void saveNewMap() {
        // Create a new repository instance
        var repository = new MapRepository(System.getProperty("user.dir") + "/src/main/resources/data/maps.ser");

        // Create a new map
        var bind = createMap("Bind");

        // Save the new map
        repository.save(bind);

        // Assert that the map was saved successfully
        assertTrue(bind.getId() > 0);
        assertNotNull(repository.get(bind.getId()).getType());

        // Reload repository and verify saved map
        repository.load();
        assertNotNull(repository.get(bind.getId()));
        assertNotNull(repository.get(bind.getId()).getType());
    }

    // Test case for deleting a map
    @Test
    void deleteMap() throws IOException {
        // Define paths for temporary and test data files
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/test-delete-maps.ser";
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/delete-maps.ser";

        // Copy the test data file to the temporary location
        Files.copy(Path.of(testDataPath), Path.of(dataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a repository instance with the temporary data file
        var repository = new MapRepository(dataPath);

        // Define the ID of the map to delete
        int id = 1;

        // Create a new map instance
        var mapToDelete = new com.valorant.file.models.Map();
        mapToDelete.setId(id);

        // Save the map to the repository
        repository.save(mapToDelete);

        // Verify that the map exists before deletion
        assertNotNull(repository.get(id));

        // Delete the map
        repository.delete(mapToDelete);

        // Verify that the map has been deleted
        assertNull(repository.get(id));
    }

    // Test case for retrieving a map
    @Test
    void getMap() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/get-maps.ser";

        // Create the repository using the data file path
        var repository = new MapRepository(dataPath);

        // Create a new map instance
        var map = createMap("Ascent");

        // Save the map to the repository
        repository.save(map);

        // Attempt to retrieve the map from the repository and verify that it is not null
        assertNotNull(repository.get(map.getId()));
    }

    // Test case for retrieving all maps
    @Test
    void getAllMaps() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/getAll-maps.ser";

        // Create the repository using the data file path
        var repository = new MapRepository(dataPath);

        // Clear all maps from the repository
        repository.getAll().forEach(repository::delete);

        // Create multiple map instances
        var map1 = createMap("Haven");
        var map2 = createMap("Icebox");
        var map3 = createMap("Split");

        // Save the maps to the repository
        repository.save(map1);
        repository.save(map2);
        repository.save(map3);

        // Check if we can retrieve all maps from the repository
        assertEquals(3, repository.getAll().size());
    }

    // Test case for retrieving a map by name
    @Test
    void getMapByName() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/map-tests/get-maps.ser";

        // Create the repository using the data file path
        var repository = new MapRepository(dataPath);

        // Create a new map instance
        var map = createMap("Ascent");

        // Save the map to the repository
        repository.save(map);

        // Attempt to retrieve the map by name from the repository and verify that it is not null
        assertNotNull(repository.getByName(map.getName()));
    }

    // Helper method to create a new Map instance
    private com.valorant.file.models.Map createMap(String name) {
        var map = new com.valorant.file.models.Map();
        map.setName(name);
        map.setType("Standard");
        return map;
    }
}
