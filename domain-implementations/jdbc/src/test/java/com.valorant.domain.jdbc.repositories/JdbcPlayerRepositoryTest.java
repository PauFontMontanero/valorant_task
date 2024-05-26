package com.valorant.domain.jdbc.repositories;

import com.valorant.dbtestutils.db.DbUtils;
import com.valorant.models.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit(); // Commit transaction
            connection.setAutoCommit(true); // Restore auto-commit mode
            connection.close();
        }
    }

    private static void insertDataIfNotExist() throws SQLException {
        insertWeaponsIfNotExist();
        insertAgentsIfNotExist();
        insertMapsIfNotExist();
        insertMatchesIfNotExist();
    }

    private static void insertWeaponsIfNotExist() throws SQLException {
        Set<Weapon> weapons = weaponRepository.getAll();
        if (weapons.isEmpty()) {
            Weapon weapon1 = new WeaponImpl(0, "Phantom", "Rifle");
            Weapon weapon2 = new WeaponImpl(0, "Vandal", "Rifle");
            weaponRepository.save(weapon1);
            weaponRepository.save(weapon2);
        }
    }

    private static void insertAgentsIfNotExist() {
        Set<Agent> agents = agentRepository.getAll();
        if (agents.isEmpty()) {
            Agent agent1 = new AgentImpl();
            agent1.setName("Jett");
            agent1.setDescription("A duelist with great mobility and offensive capabilities.");
            agent1.setRole("Duelist");

            Agent agent2 = new AgentImpl();
            agent2.setName("Sage");
            agent2.setDescription("A sentinel with healing capabilities.");
            agent2.setRole("Sentinel");

            agentRepository.save(agent1);
            agentRepository.save(agent2);
        }
    }

    private static void insertMapsIfNotExist() {
        Set<Map> maps = mapRepository.getAll();
        if (maps.isEmpty()) {
            Map map1 = new MapImpl();
            map1.setName("Ascent");
            map1.setType("Competitive");
            mapRepository.save(map1);
        }
    }

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

    @Nested
    @DisplayName("Save Player Tests")
    class SavePlayerTests {

        @Test
        @DisplayName("Given a new player, when saved, then the player should have a valid ID")
        void saveNewPlayerTest() {
            Player player = new PlayerImpl();
            player.setUsername("testuser");
            player.setDisplayName("Test User");
            player.setEmail("testuser@example.com");
            player.setRegion("NA");
            player.setRank("Silver 2");

            playerRepository.save(player);

            assertTrue(player.getId() > 0, "Player ID should be greater than 0");
        }
    }

    @Nested
    @DisplayName("Update Player Tests")
    class UpdatePlayerTests {

        @Test
        @DisplayName("Given an existing player with modified fields, when saved, then the player should be updated")
        void updatePlayerTest() {
            Player player = new PlayerImpl();
            player.setUsername("updateuser");
            player.setDisplayName("Update User");
            player.setEmail("updateuser@example.com");
            player.setRegion("EU");
            player.setRank("Gold 1");

            playerRepository.save(player);

            player.setEmail("newemail@example.com");
            playerRepository.save(player);

            Player updatedPlayer = playerRepository.get(player.getId());
            assertNotNull(updatedPlayer, "Updated player should not be null");
            assertEquals("newemail@example.com", updatedPlayer.getEmail(), "Player email should be updated");
        }
    }

    @Nested
    @DisplayName("Delete Player Tests")
    class DeletePlayerTests {

        @Test
        @DisplayName("Given a player, when deleted, then the player should not exist in the repository")
        void deletePlayerTest() {
            Player player = new PlayerImpl();
            player.setUsername("deleteuser");
            player.setDisplayName("Delete User");
            player.setEmail("deleteuser@example.com");
            player.setRegion("NA");
            player.setRank("Bronze 3");

            playerRepository.save(player);

            playerRepository.delete(player);

            assertNull(playerRepository.get(player.getId()), "Deleted player should be null");
        }

        @Test
        @DisplayName("Given a non-existent player, when deleted, then no exception should be thrown")
        void deleteNonExistentPlayerTest() {
            Player player = new PlayerImpl();
            player.setId(9999);

            assertDoesNotThrow(() -> playerRepository.delete(player), "Deleting a non-existent player should not throw an exception");
        }
    }

    @Nested
    @DisplayName("Retrieve Player Tests")
    class RetrievePlayerTests {

        @Test
        @DisplayName("Given a player ID, when retrieved, then the correct player should be returned")
        void getPlayerTest() {
            Player player = new PlayerImpl();
            player.setUsername("retrieveme");
            player.setDisplayName("Retrieve Me");
            player.setEmail("retrieveme@example.com");
            player.setRegion("EU");
            player.setRank("Gold 1");

            playerRepository.save(player);
            int playerId = player.getId();

            Player retrievedPlayer = playerRepository.get(playerId);

            assertNotNull(retrievedPlayer, "Player should not be null");
            assertEquals(playerId, retrievedPlayer.getId(), "Player ID should match the expected ID");
        }

        @Test
        @DisplayName("Given a non-existent player ID, when retrieved, then null should be returned")
        void getPlayerByNonExistentIdTest() {
            int nonExistentId = 9999;

            Player player = playerRepository.get(nonExistentId);

            assertNull(player, "Retrieving a non-existent player ID should return null");
        }

        @Test
        @DisplayName("When all players are retrieved, then the set should not be empty")
        void getAllPlayersTest() {
            Set<Player> players = playerRepository.getAll();

            assertFalse(players.isEmpty(), "The set of players should not be empty");
        }

        @Test
        @DisplayName("Given a username, when retrieved, then the correct player should be returned")
        void getPlayerByUsernameTest() {
            Player player = new PlayerImpl();
            player.setUsername("username");
            player.setDisplayName("Username");
            player.setEmail("username@example.com");
            player.setRegion("NA");
            player.setRank("Platinum 1");

            playerRepository.save(player);

            Player retrievedPlayer = playerRepository.getByUsername("username");

            assertNotNull(retrievedPlayer, "Player should not be null");
            assertEquals("username", retrievedPlayer.getUsername(), "Player username should match the expected username");
        }

        @Test
        @DisplayName("Given a region, when retrieved, then the correct players should be returned")
        void getPlayersByRegionTest() {
            String region = "NA";

            Player player1 = new PlayerImpl();
            player1.setUsername("player1");
            player1.setDisplayName("Player 1");
            player1.setEmail("player1@example.com");
            player1.setRegion(region);
            player1.setRank("Silver 1");

            Player player2 = new PlayerImpl();
            player2.setUsername("player2");
            player2.setDisplayName("Player 2");
            player2.setEmail("player2@example.com");
            player2.setRegion(region);
            player2.setRank("Gold 1");

            playerRepository.save(player1);
            playerRepository.save(player2);

            Set<Player> players = playerRepository.getByRegion(region);

            assertNotNull(players, "The set of players should not be null");
            assertFalse(players.isEmpty(), "The set of players should not be empty");
        }

        @Test
        @DisplayName("Given a display name, when retrieved, then the correct players should be returned")
        void getPlayersByDisplayNameTest() {
            String displayName = "DisplayName";

            Player player1 = new PlayerImpl();
            player1.setUsername("player1");
            player1.setDisplayName(displayName);
            player1.setEmail("player1@example.com");
            player1.setRegion("NA");
            player1.setRank("Silver 1");

            Player player2 = new PlayerImpl();
            player2.setUsername("player2");
            player2.setDisplayName(displayName);
            player2.setEmail("player2@example.com");
            player2.setRegion("NA");
            player2.setRank("Gold 1");

            playerRepository.save(player1);
            playerRepository.save(player2);

            Set<Player> players = playerRepository.getByDisplayName(displayName);

            assertNotNull(players, "The set of players should not be null");
            assertFalse(players.isEmpty(), "The set of players should not be empty");
        }
    }

    @Nested
    @DisplayName("Player Match Relationship Tests")
    class PlayerMatchRelationshipTests {

        @Test
        @DisplayName("Given a player and a match, when player is added to match, then the relationship should be saved")
        void addPlayerToMatchTest() {
            Player player = new PlayerImpl();
            player.setUsername("matchplayer");
            player.setDisplayName("Match Player");
            player.setEmail("matchplayer@example.com");
            player.setRegion("EU");
            player.setRank("Gold 3");
            playerRepository.save(player);

            Match match = new MatchImpl();
            match.setPlayedOn(LocalDateTime.now());
            match.setMapId(1); // Assuming map with ID 1 exists
            match.setOutcome("Victory");
            try {
                matchRepository.save(match);
                matchPlayerRepository.addPlayerToMatch(player.getId(), match.getId());
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }

            try {
                Set<Match> matches = matchPlayerRepository.getMatchesByPlayerId(player.getId());
                assertFalse(matches.isEmpty(), "The set of matches should not be empty");
                assertTrue(matches.stream().anyMatch(m -> m.getId() == match.getId()), "The match should be associated with the player");
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Player Agent Relationship Tests")
    class PlayerAgentRelationshipTests {

        @Test
        @DisplayName("Given a player and an agent, when agent is assigned to player, then the relationship should be saved")
        void assignAgentToPlayerTest() {
            Player player = new PlayerImpl();
            player.setUsername("agentplayer");
            player.setDisplayName("Agent Player");
            player.setEmail("agentplayer@example.com");
            player.setRegion("EU");
            player.setRank("Silver 3");
            playerRepository.save(player);

            Agent agent = new AgentImpl();
            agent.setName("Phoenix");
            agent.setDescription("A duelist with fire-based abilities.");
            agent.setRole("Duelist");
            agentRepository.save(agent);

            try {
                // Ensure the player-agent relationship entry exists before the assignment
                playerAgentRepository.assignAgentToPlayer(player.getId(), agent.getId());
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }

            Agent assignedAgent = null;
            try {
                assignedAgent = playerAgentRepository.getAgentByPlayerId(player.getId());
                assertNotNull(assignedAgent, "Assigned agent should not be null");
                assertEquals(agent.getId(), assignedAgent.getId(), "Assigned agent ID should match the expected agent ID");
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Player Weapon Relationship Tests")
    class PlayerWeaponRelationshipTests {

        @Test
        @DisplayName("Given a player and a weapon, when weapon is assigned to player, then the relationship should be saved")
        void assignWeaponToPlayerTest() {
            Player player = new PlayerImpl();
            player.setUsername("weaponplayer");
            player.setDisplayName("Weapon Player");
            player.setEmail("weaponplayer@example.com");
            player.setRegion("NA");
            player.setRank("Gold 2");
            playerRepository.save(player);

            Weapon weapon = new WeaponImpl(0, "Operator", "Sniper Rifle");  // This should be "Sniper Rifle"
            weaponRepository.save(weapon);

            try {
                playerWeaponRepository.assignWeaponToPlayer(player.getId(), weapon.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Weapon assignedWeapon;
            try {
                assignedWeapon = playerWeaponRepository.getWeaponByPlayerId(player.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            assertNotNull(assignedWeapon, "Assigned weapon should not be null");
            assertEquals(weapon.getId(), assignedWeapon.getId(), "Assigned weapon ID should match the expected weapon ID");
        }
    }
}
