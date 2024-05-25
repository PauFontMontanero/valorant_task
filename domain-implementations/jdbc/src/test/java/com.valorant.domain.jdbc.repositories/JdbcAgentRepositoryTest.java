package com.valorant.domain.jdbc.repositories;

import com.valorant.models.Agent;
import com.valorant.models.AgentImpl;
import com.valorant.dbtestutils.db.DbUtils;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the JdbcAgentRepository.
 */
class JdbcAgentRepositoryTest {

    private Connection connection;
    private JdbcAgentRepository agentRepository;

    /**
     * Sets up the database connection and starts a transaction before each test.
     *
     * @throws SQLException if a database access error occurs
     */
    @BeforeEach
    void setUp() throws SQLException {
        connection = DbUtils.connectToDb();
        connection.setAutoCommit(false); // Start transaction
        agentRepository = new JdbcAgentRepository(connection);
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
     * Tests for agent saving functionality.
     */
    @Nested
    @DisplayName("Save Agent Tests")
    class SaveAgentTests {

        /**
         * Tests if a new agent is correctly saved and assigned a valid ID.
         */
        @Test
        @DisplayName("Given a new agent, when saved, then the agent should have a valid ID")
        void saveNewAgentTest() {
            // Arrange
            Agent agent = new AgentImpl();
            agent.setName("Jett");
            agent.setDescription("A duelist with great mobility and offensive capabilities.");
            agent.setRole("Duelist");

            // Act
            agentRepository.save(agent);

            // Assert
            assertTrue(agent.getId() > 0, "Agent ID should be greater than 0");
        }

        /**
         * Tests if the repository correctly handles agents with special characters in their names.
         */
        @Test
        @DisplayName("Given an agent with special characters in the name, when saved and retrieved, then the correct agent should be returned")
        void saveAndRetrieveAgentWithSpecialCharactersTest() {
            // Arrange
            Agent agent = new AgentImpl();
            agent.setName("Reyna@Valorant");
            agent.setDescription("A duelist with vampiric abilities.");
            agent.setRole("Duelist");

            // Act
            agentRepository.save(agent);
            Agent retrievedAgent = agentRepository.getByName("Reyna@Valorant");

            // Assert
            assertNotNull(retrievedAgent, "Agent should not be null");
            assertEquals("Reyna@Valorant", retrievedAgent.getName(), "Agent name should match the expected name");
            assertEquals("A duelist with vampiric abilities.", retrievedAgent.getDescription(), "Agent description should match the expected description");
            assertEquals("Duelist", retrievedAgent.getRole(), "Agent role should match the expected role");
        }
    }

    /**
     * Tests for agent updating functionality.
     */
    @Nested
    @DisplayName("Update Agent Tests")
    class UpdateAgentTests {

        /**
         * Tests if an existing agent with modified fields is correctly updated.
         */
        @Test
        @DisplayName("Given an existing agent with modified fields, when saved, then the agent should be updated")
        void updateAgentTest() {
            // Arrange
            Agent agent = new AgentImpl();
            agent.setName("Phoenix");
            agent.setDescription("A duelist with fire-based abilities.");
            agent.setRole("Duelist");

            agentRepository.save(agent);

            agent.setName("Updated Phoenix");
            agent.setDescription("An updated duelist with fire-based abilities.");

            // Act
            agentRepository.save(agent);

            // Assert
            Agent updatedAgent = agentRepository.get(agent.getId());
            assertNotNull(updatedAgent, "Updated agent should not be null");
            assertEquals("Updated Phoenix", updatedAgent.getName(), "Agent name should be updated");
            assertEquals("An updated duelist with fire-based abilities.", updatedAgent.getDescription(), "Agent description should be updated");
            assertEquals("Duelist", updatedAgent.getRole(), "Agent role should be updated");
        }
    }

    /**
     * Tests for agent deleting functionality.
     */
    @Nested
    @DisplayName("Delete Agent Tests")
    class DeleteAgentTests {

        /**
         * Tests if an agent is correctly deleted.
         */
        @Test
        @DisplayName("Given an agent, when deleted, then the agent should not exist in the repository")
        void deleteAgentTest() {
            // Arrange
            Agent agent = new AgentImpl();
            agent.setName("Sova");
            agent.setDescription("An initiator with recon abilities.");
            agent.setRole("Initiator");

            agentRepository.save(agent);

            // Act
            agentRepository.delete(agent);

            // Assert
            assertNull(agentRepository.get(agent.getId()), "Deleted agent should be null");
        }

        /**
         * Tests if deleting a non-existent agent does not throw an exception.
         */
        @Test
        @DisplayName("Given a non-existent agent, when deleted, then no exception should be thrown")
        void deleteNonExistentAgentTest() {
            // Arrange
            Agent agent = new AgentImpl();
            agent.setId(9999);

            // Act & Assert
            assertDoesNotThrow(() -> agentRepository.delete(agent), "Deleting a non-existent agent should not throw an exception");
        }
    }

    /**
     * Tests for agent retrieval functionality.
     */
    @Nested
    @DisplayName("Retrieve Agent Tests")
    class RetrieveAgentTests {

        /**
         * Tests if an agent is correctly retrieved by ID.
         */
        @Test
        @DisplayName("Given an agent ID, when retrieved, then the correct agent should be returned")
        void getAgentTest() {
            // Arrange
            Agent agent = new AgentImpl();
            agent.setName("Sage");
            agent.setDescription("A sentinel with healing abilities.");
            agent.setRole("Sentinel");

            agentRepository.save(agent);
            int agentId = agent.getId();

            // Act
            Agent retrievedAgent = agentRepository.get(agentId);

            // Assert
            assertNotNull(retrievedAgent, "Agent should not be null");
            assertEquals(agentId, retrievedAgent.getId(), "Agent ID should match the expected ID");
        }

        /**
         * Tests if an agent is correctly retrieved by name.
         */
        @Test
        @DisplayName("Given an agent name, when retrieved, then the correct agent should be returned")
        void getAgentByNameTest() {
            // Arrange
            Agent agent = new AgentImpl();
            agent.setName("Jett");
            agent.setDescription("A duelist with great mobility and offensive capabilities.");
            agent.setRole("Duelist");

            agentRepository.save(agent);

            // Act
            Agent retrievedAgent = agentRepository.getByName("Jett");

            // Assert
            assertNotNull(retrievedAgent, "Agent should not be null");
            assertEquals("Jett", retrievedAgent.getName(), "Agent name should match the expected name");
        }

        /**
         * Tests if retrieving an agent by a non-existent ID returns null.
         */
        @Test
        @DisplayName("Given a non-existent agent ID, when retrieved, then null should be returned")
        void getAgentByNonExistentIdTest() {
            // Arrange
            int nonExistentId = 9999;

            // Act
            Agent agent = agentRepository.get(nonExistentId);

            // Assert
            assertNull(agent, "Retrieving a non-existent agent ID should return null");
        }

        /**
         * Tests if retrieving an agent by a non-existent name returns null.
         */
        @Test
        @DisplayName("Given a non-existent agent name, when retrieved, then null should be returned")
        void getAgentByNonExistentNameTest() {
            // Arrange
            String nonExistentName = "NonExistentAgent";

            // Act
            Agent agent = agentRepository.getByName(nonExistentName);

            // Assert
            assertNull(agent, "Retrieving a non-existent agent name should return null");
        }

        /**
         * Tests if all agents are correctly retrieved.
         */
        @Test
        @DisplayName("When all agents are retrieved, then the set should not be empty")
        void getAllAgentsTest() {
            // Arrange

            // Act
            Set<Agent> agents = agentRepository.getAll();

            // Assert
            assertFalse(agents.isEmpty(), "The set of agents should not be empty");
        }
    }
}
