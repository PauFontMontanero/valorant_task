package com.valorant.domain.jdbc.repositories;

import com.valorant.dbtestutils.db.DbUtils;
import com.valorant.models.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JdbcPlayerRepository. This class contains nested test suites
 * to verify the CRUD operations and relationships involving players in the repository.
 */
class JdbcPlayerRepositoryTest {

    private static Connection connection;
    private static JdbcPlayerRepository playerRepository;
    private static JdbcWeaponRepository weaponRepository;
    private static JdbcAgentRepository agentRepository;
    private static JdbcMatchRepository matchRepository;
    private static JdbcMapRepository mapRepository;
    private static JdbcMatchPlayerRepository matchPlayerRepository;
    private static JdbcPlayerAgentRepository playerAgentRepository;
    private static JdbcPlayerWeaponRepository playerWeaponRepository;

    /**
     * Sets up the database connection and repositories before all tests.
     *
     * @throws SQLException if there is an error connecting to the database
     */
    @BeforeAll
    static void setUp() throws SQLException {
        connection = DbUtils.connectToDb();
        connection.setAutoCommit(false); // Start transaction
        playerRepository = new JdbcPlayerRepository(connection);
        weaponRepository = new JdbcWeaponRepository(connection);
        agentRepository = new JdbcAgentRepository(connection);
        matchRepository = new JdbcMatchRepository(connection);
        mapRepository = new JdbcMapRepository(connection);

        // Pass the required repositories to the constructors
        matchPlayerRepository = new JdbcMatchPlayerRepository(connection, matchRepository);
        playerAgentRepository = new JdbcPlayerAgentRepository(connection, agentRepository);
        playerWeaponRepository = new JdbcPlayerWeaponRepository(connection, weaponRepository);

        insertDataIfNotExist();
    }

    /**
     * Commits the transaction and closes the database connection after all tests.
     *
     * @throws SQLException if there is an error closing the connection
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
     * Inserts initial data into the database if it does not already exist.
     *
     */
    private static void insertDataIfNotExist() {
        insertWeaponsIfNotExist();
        insertAgentsIfNotExist();
        insertMapsIfNotExist();
        insertMatchesIfNotExist();
    }

    /**
     * Inserts initial weapons into the database if they do not already exist.
     *
     */
    private static void insertWeaponsIfNotExist() {
        Set<Weapon> weapons = weaponRepository.getAll();
        if (weapons.isEmpty()) {
            Weapon weapon1 = new WeaponImpl(0, "Phantom", "Rifle");
            Weapon weapon2 = new WeaponImpl(0, "Vandal", "Rifle");
            Weapon weapon3 = new WeaponImpl(0, "Operator", "Sniper Rifle");
            weaponRepository.save(weapon1);
            weaponRepository.save(weapon2);
            weaponRepository.save(weapon3);
        }
    }

    /**
     * Inserts initial agents into the database if they do not already exist.
     */
    private static void insertAgentsIfNotExist() {
        Set<Agent> agents = agentRepository.getAll();
        if (agents.isEmpty()) {
            Agent agent1 = new AgentImpl();
            agent1.setName("Jett");
            agent1.setDescription("Representing her home country of South Korea, Jett's agile and evasive fighting style lets her take risks no one else can. She runs circles around every skirmish, cutting enemies before they even know what hit them.");
            agent1.setRole("Duelist");

            Agent agent2 = new AgentImpl();
            agent2.setName("Sage");
            agent2.setDescription("The stronghold of China, Sage creates safety for herself and her team wherever she goes. Able to revive fallen friends and stave off aggressive pushes, she provides a calm center to a hellish fight.");
            agent2.setRole("Sentinel");

            agentRepository.save(agent1);
            agentRepository.save(agent2);
        }
    }

    /**
     * Inserts initial maps into the database if they do not already exist.
     */
    private static void insertMapsIfNotExist() {
        Set<Map> maps = mapRepository.getAll();
        if (maps.isEmpty()) {
            Map map1 = new MapImpl();
            map1.setName("Ascent");
            map1.setType("Competitive");
            mapRepository.save(map1);
        }
    }

    /**
     * Inserts initial matches into the database if they do not already exist.
     */
    private static void insertMatchesIfNotExist() {
        Set<Match> matches = matchRepository.getAll();
        if (matches.isEmpty()) {
            Match match1 = new MatchImpl();
            match1.setPlayedOn(LocalDateTime.now());
            match1.setMapId(1); // Assuming map with ID 1 exists
            match1.setOutcome("Victory");
            matchRepository.save(match1);
        }
    }

    /**
     * Test suite for saving players.
     */
    @Nested
    @DisplayName("Save Player Tests")
    class SavePlayerTests {

        /**
         * Test saving a new player.
         */
        @Test
        @DisplayName("Given a new player, when saved, then the player should have a valid ID")
        void saveNewPlayerTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("ItzSebiii");
            player.setDisplayName("ItzSebiii");
            player.setEmail("bladesilviu526@gmail.com");
            player.setRegion("EUW");
            player.setRank("Immortal 3");

            // Act
            playerRepository.save(player);

            // Assert
            assertTrue(player.getId() > 0, "Player ID should be greater than 0");
        }
    }

    /**
     * Test suite for updating players.
     */
    @Nested
    @DisplayName("Update Player Tests")
    class UpdatePlayerTests {

        /**
         * Test updating an existing player.
         */
        @Test
        @DisplayName("Given an existing player with modified fields, when saved, then the player should be updated")
        void updatePlayerTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("TenZ");
            player.setDisplayName("SEN TenZ");
            player.setEmail("ngotenz@gmail.com");
            player.setRegion("NA");
            player.setRank("Radiant");

            playerRepository.save(player);

            // Act
            player.setEmail("sentenzngo@gmail.com");
            playerRepository.save(player);

            // Assert
            Player updatedPlayer = playerRepository.get(player.getId());
            assertNotNull(updatedPlayer, "Updated player should not be null");
            assertEquals("sentenzngo@gmail.com", updatedPlayer.getEmail(), "Player email should be updated");
        }
    }

    /**
     * Test suite for deleting players.
     */
    @Nested
    @DisplayName("Delete Player Tests")
    class DeletePlayerTests {

        /**
         * Test deleting a player.
         */
        @Test
        @DisplayName("Given a player, when deleted, then the player should not exist in the repository")
        void deletePlayerTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("deleteuser");
            player.setDisplayName("Delete User");
            player.setEmail("deleteuser@gmail.com");
            player.setRegion("NA");
            player.setRank("Iron 3");

            playerRepository.save(player);

            // Act
            playerRepository.delete(player);

            // Assert
            assertNull(playerRepository.get(player.getId()), "Deleted player should be null");
        }

        /**
         * Test deleting a non-existent player.
         */
        @Test
        @DisplayName("Given a non-existent player, when deleted, then no exception should be thrown")
        void deleteNonExistentPlayerTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setId(9999);

            // Act & Assert
            assertDoesNotThrow(() -> playerRepository.delete(player), "Deleting a non-existent player should not throw an exception");
        }
    }

    /**
     * Test suite for retrieving players.
     */
    @Nested
    @DisplayName("Retrieve Player Tests")
    class RetrievePlayerTests {

        /**
         * Test retrieving a player by ID.
         */
        @Test
        @DisplayName("Given a player ID, when retrieved, then the correct player should be returned")
        void getPlayerTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("Aspas");
            player.setDisplayName("Lev Aspas");
            player.setEmail("aspaserick@gmail.com");
            player.setRegion("APAC");
            player.setRank("Radiant");

            playerRepository.save(player);
            int playerId = player.getId();

            // Act
            Player retrievedPlayer = playerRepository.get(playerId);

            // Assert
            assertNotNull(retrievedPlayer, "Player should not be null");
            assertEquals(playerId, retrievedPlayer.getId(), "Player ID should match the expected ID");
        }

        /**
         * Test retrieving a player by non-existent ID.
         */
        @Test
        @DisplayName("Given a non-existent player ID, when retrieved, then null should be returned")
        void getPlayerByNonExistentIdTest() {
            // Act
            int nonExistentId = 9999;
            Player player = playerRepository.get(nonExistentId);

            // Assert
            assertNull(player, "Retrieving a non-existent player ID should return null");
        }

        /**
         * Test retrieving all players.
         */
        @Test
        @DisplayName("When all players are retrieved, then the set should not be empty")
        void getAllPlayersTest() {
            // Act
            Set<Player> players = playerRepository.getAll();

            // Assert
            assertFalse(players.isEmpty(), "The set of players should not be empty");
        }

        /**
         * Test retrieving a player by username.
         */
        @Test
        @DisplayName("Given a username, when retrieved, then the correct player should be returned")
        void getPlayerByUsernameTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("lansito26");
            player.setDisplayName("ItzLandros");
            player.setEmail("david26_landros@gmail.com");
            player.setRegion("EUW");
            player.setRank("Gold 3");

            playerRepository.save(player);

            // Act
            Player retrievedPlayer = playerRepository.getByUsername("lansito26");

            // Assert
            assertNotNull(retrievedPlayer, "Player should not be null");
            assertEquals("lansito26", retrievedPlayer.getUsername(), "Player username should match the expected username");
        }

        /**
         * Test retrieving players by region.
         */
        @Test
        @DisplayName("Given a region, when retrieved, then the correct players should be returned")
        void getPlayersByRegionTest() {
            // Arrange
            String region = "NA";

            Player player1 = new PlayerImpl();
            player1.setUsername("joeletspetit123");
            player1.setDisplayName("maaleluia");
            player1.setEmail("maaleluia_terror_en_coche@gmail.com");
            player1.setRegion(region);
            player1.setRank("Silver 1");

            Player player2 = new PlayerImpl();
            player2.setUsername("salas12");
            player2.setDisplayName("ivancito12");
            player2.setEmail("salas2_crypto@gmail.com");
            player2.setRegion(region);
            player2.setRank("Gold 1");

            playerRepository.save(player1);
            playerRepository.save(player2);

            // Act
            Set<Player> players = playerRepository.getByRegion(region);

            // Assert
            assertNotNull(players, "The set of players should not be null");
            assertFalse(players.isEmpty(), "The set of players should not be empty");
        }

        /**
         * Test retrieving players by display name.
         */
        @Test
        @DisplayName("Given a display name, when retrieved, then the correct players should be returned")
        void getPlayersByDisplayNameTest() {
            // Arrange
            String displayName = "Elrubiusomg";
            String displayName2 = "Vegetta777";

            Player player1 = new PlayerImpl();
            player1.setUsername("elrubius777");
            player1.setDisplayName(displayName);
            player1.setEmail("rubenangel@gmail.com");
            player1.setRegion("EUW");
            player1.setRank("Gold 2");

            Player player2 = new PlayerImpl();
            player2.setUsername("vegetta_goku123");
            player2.setDisplayName(displayName2);
            player2.setEmail("vegettaeselmejor@gmail.com");
            player2.setRegion("EUW");
            player2.setRank("Diamond 1");

            playerRepository.save(player1);
            playerRepository.save(player2);

            // Act
            Set<Player> players = playerRepository.getByDisplayName(displayName);

            // Assert
            assertNotNull(players, "The set of players should not be null");
            assertFalse(players.isEmpty(), "The set of players should not be empty");
        }
    }

    /**
     * Test suite for player-match relationships.
     */
    @Nested
    @DisplayName("Player Match Relationship Tests")
    class PlayerMatchRelationshipTests {

        /**
         * Test adding a player to a match.
         */
        @Test
        @DisplayName("Given a player and a match, when player is added to match, then the relationship should be saved")
        void addPlayerToMatchTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("Iawofcycles");
            player.setDisplayName("wyattloves69");
            player.setEmail("iawofcycles@hotmail.com");
            player.setRegion("NA");
            player.setRank("Platinum 3");
            playerRepository.save(player);

            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Assuming map with ID 1 exists
            match.setOutcome("Victory");

            // Act
            try {
                matchRepository.save(match);
                matchPlayerRepository.addPlayerToMatch(player.getId(), match.getId());
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }

            // Assert
            try {
                Set<Match> matches = matchPlayerRepository.getMatchesByPlayerId(player.getId());
                assertFalse(matches.isEmpty(), "The set of matches should not be empty");
                assertTrue(matches.stream().anyMatch(m -> m.getId() == match.getId()), "The match should be associated with the player");
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Test suite for player-agent relationships.
     */
    @Nested
    @DisplayName("Player Agent Relationship Tests")
    class PlayerAgentRelationshipTests {

        /**
         * Test assigning an agent to a player.
         */
        @Test
        @DisplayName("Given a player and an agent, when agent is assigned to player, then the relationship should be saved")
        void assignAgentToPlayerTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("mickael123");
            player.setDisplayName("MickaelJackson");
            player.setEmail("mickaelgotsomegyatt@gmail.com");
            player.setRegion("EUW");
            player.setRank("Immortal 1");
            playerRepository.save(player);

            Agent agent = new AgentImpl();
            agent.setName("Jett");
            agent.setDescription("Representing her home country of South Korea, Jett's agile and evasive fighting style lets her take risks no one else can. She runs circles around every skirmish, cutting enemies before they even know what hit them.");
            agent.setRole("Duelist");
            agentRepository.save(agent);

            // Act
            try {
                playerAgentRepository.assignAgentToPlayer(player.getId(), agent.getId());
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }

            // Assert
            Agent assignedAgent;
            try {
                assignedAgent = playerAgentRepository.getAgentByPlayerId(player.getId());
                assertNotNull(assignedAgent, "Assigned agent should not be null");
                assertEquals(agent.getId(), assignedAgent.getId(), "Assigned agent ID should match the expected agent ID");
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Test suite for player-weapon relationships.
     */
    @Nested
    @DisplayName("Player Weapon Relationship Tests")
    class PlayerWeaponRelationshipTests {

        /**
         * Test assigning a weapon to a player.
         */
        @Test
        @DisplayName("Given a player and a weapon, when weapon is assigned to player, then the relationship should be saved")
        void assignWeaponToPlayerTest() {
            // Arrange
            Player player = new PlayerImpl();
            player.setUsername("sagethebattlegirl");
            player.setDisplayName("BattleSage");
            player.setEmail("iamthebattlesage@gmail.com");
            player.setRegion("EUW");
            player.setRank("Radiant");
            playerRepository.save(player);

            Weapon weapon = new WeaponImpl(0, "Operator", "Sniper Rifle");
            weaponRepository.save(weapon);

            // Act
            try {
                playerWeaponRepository.assignWeaponToPlayer(player.getId(), weapon.getId());
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }

            // Assert
            Weapon assignedWeapon;
            try {
                assignedWeapon = playerWeaponRepository.getWeaponByPlayerId(player.getId());
                assertNotNull(assignedWeapon, "Assigned weapon should not be null");
                assertEquals(weapon.getId(), assignedWeapon.getId(), "Assigned weapon ID should match the expected weapon ID");
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }
        }
    }
}
