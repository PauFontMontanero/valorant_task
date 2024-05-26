package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Weapon;
import com.valorant.models.WeaponImpl;
import com.valorant.dbtestutils.db.DbUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the JdbcWeaponRepository.
 */
class JdbcWeaponRepositoryTest {

    private Connection connection;
    private JdbcWeaponRepository weaponRepository;

    /**
     * Sets up the database connection and starts a transaction before each test.
     *
     * @throws SQLException if a database access error occurs
     */
    @BeforeEach
    void setUp() throws SQLException {
        connection = DbUtils.connectToDb();
        connection.setAutoCommit(false); // Start transaction
        weaponRepository = new JdbcWeaponRepository(connection);

        // Insert sample data into the database
        insertSampleData();
    }

    private void insertSampleData() {
        try {
            // Sample weapon data
            Weapon weapon1 = new WeaponImpl(0, "Phantom", "Rifle");
            Weapon weapon2 = new WeaponImpl(0, "Vandal", "Rifle");

            // Save the sample weapons
            weaponRepository.save(weapon1);
            weaponRepository.save(weapon2);

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            // Rollback transaction if an exception occurs
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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
     * Tests for weapon saving functionality.
     */
    @Nested
    @DisplayName("Save Weapon Tests")
    class SaveWeaponTests {

        /**
         * Tests if a new weapon is correctly saved and assigned a valid ID.
         */
        @Test
        @DisplayName("Given a new weapon, when saved, then the weapon should have a valid ID")
        void saveNewWeaponTest() {
            // Arrange
            Weapon weapon = new WeaponImpl(0, "Phantom", "Rifle");

            // Act
            weaponRepository.save(weapon);

            // Assert
            assertTrue(weapon.getId() > 0, "Weapon ID should be greater than 0");
        }
    }

    /**
     * Tests for weapon updating functionality.
     */
    @Nested
    @DisplayName("Update Weapon Tests")
    class UpdateWeaponTests {

        /**
         * Tests if an existing weapon with modified fields is correctly updated.
         */
        @Test
        @DisplayName("Given an existing weapon with modified fields, when saved, then the weapon should be updated")
        void updateWeaponTest() {
            // Arrange
            Weapon weapon = new WeaponImpl(0, "Vandal", "Rifle");

            weaponRepository.save(weapon);

            weapon = new WeaponImpl(weapon.getId(), "Updated Vandal", "Rifle"); // Using a valid type

            // Act
            weaponRepository.save(weapon);

            // Assert
            Weapon updatedWeapon = weaponRepository.get(weapon.getId());
            assertNotNull(updatedWeapon, "Updated weapon should not be null");
            assertEquals("Updated Vandal", updatedWeapon.getName(), "Weapon name should be updated");
            assertEquals("Rifle", updatedWeapon.getType(), "Weapon type should be updated"); // Asserting against the original type
        }
    }

    /**
     * Tests for weapon deleting functionality.
     */
    @Nested
    @DisplayName("Delete Weapon Tests")
    class DeleteWeaponTests {

        /**
         * Tests if a weapon is correctly deleted.
         */
        @Test
        @DisplayName("Given a weapon, when deleted, then the weapon should not exist in the repository")
        void deleteWeaponTest() {
            // Arrange
            Weapon weapon = new WeaponImpl(0, "Operator", "Sniper Rifle");

            weaponRepository.save(weapon);

            // Act
            weaponRepository.delete(weapon);

            // Assert
            assertNull(weaponRepository.get(weapon.getId()), "Deleted weapon should be null");
        }

        /**
         * Tests if deleting a non-existent weapon does not throw an exception.
         */
        @Test
        @DisplayName("Given a non-existent weapon, when deleted, then no exception should be thrown")
        void deleteNonExistentWeaponTest() {
            // Arrange
            int nonExistentId = 9999; // Assuming non-existent ID

            // Act & Assert
            assertDoesNotThrow(() -> weaponRepository.delete(new WeaponImpl(nonExistentId, null, null)),
                    "Deleting a non-existent weapon should not throw an exception");
        }
    /**
     * Tests for weapon retrieval functionality.
     */
    @Nested
    @DisplayName("Retrieve Weapon Tests")
    class RetrieveWeaponTests {

        /**
         * Tests if a weapon is correctly retrieved by ID.
         */
        @Test
        @DisplayName("Given a weapon ID, when retrieved, then the correct weapon should be returned")
        void getWeaponTest() {
            // Arrange
            Weapon weapon = new WeaponImpl(0, "Bucky", "Shotgun");

            weaponRepository.save(weapon);

            // Act
            Weapon retrievedWeapon = weaponRepository.get(weapon.getId());

            // Assert
            assertNotNull(retrievedWeapon, "Weapon should not be null");
            assertEquals(weapon.getId(), retrievedWeapon.getId(), "Weapon ID should match the expected ID");
            assertEquals(weapon.getName(), retrievedWeapon.getName(), "Weapon name should match the expected name");
            assertEquals(weapon.getType(), retrievedWeapon.getType(), "Weapon type should match the expected type");
        }

        /**
         * Tests if retrieving a weapon by a non-existent ID returns null.
         */
        @Test
        @DisplayName("Given a non-existent weapon ID, when retrieved, then null should be returned")
        void getWeaponByNonExistentIdTest() {
            // Arrange
            int nonExistentId = 9999;

            // Act
            Weapon weapon = weaponRepository.get(nonExistentId);

            // Assert
            assertNull(weapon, "Retrieving a non-existent weapon ID should return null");
        }

        /**
         * Tests if all weapons are correctly retrieved.
         */
        @Test
        @DisplayName("When all weapons are retrieved, then the set should not be empty")
        void getAllWeaponsTest() {
            // Arrange

            // Act
            Set<Weapon> weapons = weaponRepository.getAll();

            // Assert
            assertFalse(weapons.isEmpty(), "The set of weapons should not be empty");
        }
    }
}
}