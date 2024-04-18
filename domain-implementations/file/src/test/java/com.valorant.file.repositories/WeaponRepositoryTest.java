// This class contains test cases for the WeaponRepository class.
package com.valorant.file.repositories;

import com.valorant.file.models.Weapon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

// Test cases for WeaponRepository class
public class WeaponRepositoryTest {
    private final String testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/test-weapons.ser";
    private final String originalDataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/weapons.ser";

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

    // Test case for saving a new weapon
    @Test
    void saveNewWeapon() {
        // Create a new repository instance
        var repository = new WeaponRepository(System.getProperty("user.dir") + "/src/main/resources/data/weapons.ser");

        // Create a new weapon
        var vandal = createWeapon("Vandal", "Rifle");

        // Save the new weapon
        repository.save(vandal);

        // Assert that the weapon was saved successfully
        assertTrue(vandal.getId() > 0);
        assertNotNull(repository.get(vandal.getId()).getType());

        // Reload repository and verify saved weapon
        repository.load();
        assertNotNull(repository.get(vandal.getId()));
        assertNotNull(repository.get(vandal.getId()).getType());
    }

    // Test case for updating a weapon
    @Test
    void updateWeapon() throws IOException {
        // Copy the update data file to the test directory
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/update-weapons.ser";
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/test-update-weapons.ser";
        Files.copy(Path.of(dataPath), Path.of(testDataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a new repository instance with the test data
        var repository = new WeaponRepository(testDataPath);

        // Create and save a new weapon
        var phantom = createWeapon("Phantom", "Rifle");
        repository.save(phantom);

        // Retrieve the updated weapon
        var updatedWeapon = repository.get(1);

        // Assert that the weapon was updated successfully
        assertEquals("Phantom", updatedWeapon.getName());

        // Reload repository and verify updated weapon
        repository.load();
        var updatedWeaponFromFile = repository.get(1);
        assertEquals("Phantom", updatedWeaponFromFile.getName());
    }

    // Test case for deleting a weapon
    @Test
    void deleteWeapon() throws IOException {
        // Define paths for temporary and test data files
        var testDataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/test-delete-weapons.ser";
        var dataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/delete-weapons.ser";

        // Copy the test data file to the temporary location
        Files.copy(Path.of(testDataPath), Path.of(dataPath), StandardCopyOption.REPLACE_EXISTING);

        // Create a repository instance with the temporary data file
        var repository = new WeaponRepository(dataPath);

        // Define the ID of the weapon to delete
        int id = 1;

        // Create a new weapon instance
        var weaponToDelete = new Weapon();
        weaponToDelete.setId(id);

        // Save the weapon to the repository
        repository.save(weaponToDelete);

        // Verify that the weapon exists before deletion
        assertNotNull(repository.get(id));

        // Delete the weapon
        repository.delete(weaponToDelete);

        // Verify that the weapon has been deleted
        assertNull(repository.get(id));
    }

    // Test case for retrieving a weapon by name
    @Test
    void getByName() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/get-by-name.ser";

        // Create the repository using the data file path
        var repository = new WeaponRepository(dataPath);

        // Create a new weapon instance
        var weapon = createWeapon("Vandal", "Rifle");

        // Save the weapon to the repository
        repository.save(weapon);

        // Attempt to retrieve the weapon by name from the repository
        var retrievedWeapon = repository.getByName(weapon.getName());

        // Verify that the retrieved weapon is not null
        assertNotNull(retrievedWeapon);

        // Verify that the retrieved weapon's name matches the expected name
        assertEquals(weapon.getName(), retrievedWeapon.getName());

        // Verify that the retrieved weapon's type matches the expected type
        assertEquals(weapon.getType(), retrievedWeapon.getType());
    }

    // Test case for retrieving a weapon by ID
    @Test
    void getWeapon() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/get-weapons.ser";

        // Create the repository using the data file path
        var repository = new WeaponRepository(dataPath);

        // Create a new weapon instance
        var weapon = createWeapon("Operator", "Sniper");

        // Save the weapon to the repository
        repository.save(weapon);

        // Attempt to retrieve the weapon from the repository and verify that it is not null
        assertNotNull(repository.get(weapon.getId()));
    }

    // Test case for retrieving all weapons
    @Test
    void getAllWeapons() {
        // Define the path to the data file
        String dataPath = System.getProperty("user.dir") + "/src/test/resources/data/weapon-tests/getAll-weapons.ser";

        // Create the repository using the data file path
        var repository = new WeaponRepository(dataPath);

        // Clear all weapons from the repository
        repository.getAll().forEach(repository::delete);

        // Create multiple weapon instances
        var weapon1 = createWeapon("Sheriff", "Pistol");
        var weapon2 = createWeapon("Ghost", "Pistol");
        var weapon3 = createWeapon("Judge", "Shotgun");

        // Save the weapons to the repository
        repository.save(weapon1);
        repository.save(weapon2);
        repository.save(weapon3);

        // Check if we can retrieve all weapons from the repository
        assertEquals(3, repository.getAll().size());
    }

    // Helper method to create a new Weapon instance
    private Weapon createWeapon(String name, String type) {
        var weapon = new Weapon();
        weapon.setName(name);
        weapon.setType(type);
        return weapon;
    }
}
