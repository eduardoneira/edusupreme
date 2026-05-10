package com.edusupreme.tournament;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
