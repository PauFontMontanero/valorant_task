package com.valorant.domain.jpa.repositories;

import com.valorant.models.Weapon;
import com.valorant.models.WeaponImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JpaWeaponRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private JpaWeaponRepository weaponRepository;

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
        weaponRepository = new JpaWeaponRepository(entityManager);
        insertSampleData();
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

    private void insertSampleData() {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }

            Set<Weapon> weapons = weaponRepository.getAll();
            if (weapons.isEmpty()) {
                // Insert sample weapon data
                Weapon weapon1 = new WeaponImpl(0, "Phantom", "Rifle");
                Weapon weapon2 = new WeaponImpl(0, "Vandal", "Rifle");

                weaponRepository.save(weapon1);
                weaponRepository.save(weapon2);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving weapon", e);
        }
    }

    @Nested
    @DisplayName("Save Weapon Tests")
    class SaveWeaponTests {

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

    @Nested
    @DisplayName("Update Weapon Tests")
    class UpdateWeaponTests {

        @Test
        @DisplayName("Given an existing weapon with modified fields, when saved, then the weapon should be updated")
        void updateWeaponTest() {
            // Arrange
            Weapon weapon = new WeaponImpl(0, "Shorty", "Shotgun");

            weaponRepository.save(weapon);

            Weapon originalWeapon = weaponRepository.getByName("Shorty");

            originalWeapon.setName("Operator");
            originalWeapon.setType("Sniper Rifle");

            // Act
            weaponRepository.save(originalWeapon);

            // Assert
            Weapon updatedWeapon = weaponRepository.get(originalWeapon.getId());
            assertNotNull(updatedWeapon, "Updated weapon should not be null");
            assertEquals("Operator", updatedWeapon.getName(), "Weapon name should be updated");
            assertEquals("Sniper Rifle", updatedWeapon.getType(), "Weapon type should be updated");
        }
    }

    @Nested
    @DisplayName("Delete Weapon Tests")
    class DeleteWeaponTests {

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

        @Test
        @DisplayName("Given a non-existent weapon, when deleted, then no exception should be thrown")
        void deleteNonExistentWeaponTest() {
            // Arrange
            Weapon weapon = new WeaponImpl(9999, "NonExistent", "NonExistent");

            // Act & Assert
            assertDoesNotThrow(() -> weaponRepository.delete(weapon), "Deleting a non-existent weapon should not throw an exception");
        }
    }

    @Nested
    @DisplayName("Retrieve Weapon Tests")
    class RetrieveWeaponTests {

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
