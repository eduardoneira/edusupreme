package com.edusupreme.tournament;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TournamentApiIntegrationTests {

    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void deleteTournaments() {
        jdbcTemplate.update("DELETE FROM tournaments");
    }

    @Test
    void appliesFlywayMigrationOnStartup() {
        Integer migrationCount = jdbcTemplate.queryForObject(
                """
                SELECT count(*)
                FROM flyway_schema_history
                WHERE version = '1'
                    AND script = 'V1__create_tournaments.sql'
                    AND success = true
                """,
                Integer.class);

        assertThat(migrationCount).isEqualTo(1);
    }

    @Test
    void createsTournamentAndPersistsIt() throws Exception {
        String requestBody = """
                {
                  "name": "Madrid Spring Open",
                  "startDate": "2026-06-20",
                  "endDate": "2026-06-21",
                  "status": "SCHEDULED"
                }
                """;

        mockMvc.perform(post("/tournaments")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern("/tournaments/.+")))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Madrid Spring Open"))
                .andExpect(jsonPath("$.startDate").value("2026-06-20"))
                .andExpect(jsonPath("$.endDate").value("2026-06-21"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());

        Integer savedTournaments = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM tournaments WHERE name = ?",
                Integer.class,
                "Madrid Spring Open");

        assertThat(savedTournaments).isEqualTo(1);
    }

    @Test
    void listsTournamentsNewestFirst() throws Exception {
        insertTournament(
                UUID.fromString("018f3904-09fb-7d19-baf4-3d10a2f0bb99"),
                "Madrid Spring Open",
                LocalDate.parse("2026-06-20"),
                LocalDate.parse("2026-06-21"),
                "SCHEDULED",
                Instant.parse("2026-05-07T10:15:30Z"));
        insertTournament(
                UUID.fromString("018f3904-09fb-7d19-baf4-3d10a2f0bb9a"),
                "Barcelona Summer Cup",
                LocalDate.parse("2026-07-10"),
                LocalDate.parse("2026-07-12"),
                "DRAFT",
                Instant.parse("2026-05-08T10:15:30Z"));

        mockMvc.perform(get("/tournaments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].id").value("018f3904-09fb-7d19-baf4-3d10a2f0bb9a"))
                .andExpect(jsonPath("$.items[0].name").value("Barcelona Summer Cup"))
                .andExpect(jsonPath("$.items[0].startDate").value("2026-07-10"))
                .andExpect(jsonPath("$.items[0].endDate").value("2026-07-12"))
                .andExpect(jsonPath("$.items[0].status").value("DRAFT"))
                .andExpect(jsonPath("$.items[0].createdAt").value("2026-05-08T10:15:30Z"))
                .andExpect(jsonPath("$.items[1].id").value("018f3904-09fb-7d19-baf4-3d10a2f0bb99"))
                .andExpect(jsonPath("$.items[1].name").value("Madrid Spring Open"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalItems").value(2));
    }

    @Test
    void listsEmptyTournaments() throws Exception {
        mockMvc.perform(get("/tournaments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalItems").value(0));
    }

    @Test
    void allowsAngularDevServerToCallTournamentsApi() throws Exception {
        mockMvc.perform(options("/tournaments")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"))
                .andExpect(header().string("Access-Control-Allow-Methods", org.hamcrest.Matchers.containsString("GET")));
    }

    @Test
    void rejectsInvalidTournamentName() throws Exception {
        String requestBody = """
                {
                  "name": "  ",
                  "startDate": "2026-06-20",
                  "endDate": "2026-06-21"
                }
                """;

        mockMvc.perform(post("/tournaments")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsInvalidTournamentDateRange() throws Exception {
        String requestBody = """
                {
                  "name": "Madrid Spring Open",
                  "startDate": "2026-06-22",
                  "endDate": "2026-06-21"
                }
                """;

        mockMvc.perform(post("/tournaments")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    private void insertTournament(
            UUID id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            Instant createdAt) {
        jdbcTemplate.update(
                """
                INSERT INTO tournaments (id, name, start_date, end_date, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                id,
                name,
                startDate,
                endDate,
                status,
                Timestamp.from(createdAt));
    }
}
