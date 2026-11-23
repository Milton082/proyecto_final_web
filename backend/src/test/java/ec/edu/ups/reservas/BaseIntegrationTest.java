package ec.edu.ups.reservas;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.core.io.ClassPathResource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIntegrationTest {
    private static final Path DATABASE = createDatabase();

    @LocalServerPort
    private int port;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private EntityManager entityManager;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:sqlite:" + DATABASE);
    }

    private static Path createDatabase() {
        try {
            Path file = Files.createTempFile("reservas-integration-", ".db");
            file.toFile().deleteOnExit();
            return file;
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Test
    void apiChecksRealSqliteDatabase() throws Exception {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(HttpRequest.newBuilder(
                    URI.create("http://127.0.0.1:" + port + "/api/status")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.body()).contains("\"application\":\"reservas-laboratorios\"",
                    "\"status\":\"UP\"", "\"database\":\"UP\"");
        }
    }

    @Test
    void sqliteHasSeedForeignKeysAndJpaDialect() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getDatabaseProductName()).isEqualTo("SQLite");
        }
        assertThat(jdbc.queryForObject("PRAGMA foreign_keys", Integer.class)).isEqualTo(1);
        assertThat(entityManager.createNativeQuery("SELECT value FROM app_metadata WHERE key = 'schema_version'", String.class)
                .getSingleResult()).isEqualTo("base-1");
    }

    @Test
    void initializationScriptsCanRunTwiceWithoutLosingData() {
        ResourceDatabasePopulator scripts = new ResourceDatabasePopulator(
                new ClassPathResource("db/schema.sql"), new ClassPathResource("db/seed.sql"));
        scripts.execute(dataSource);
        scripts.execute(dataSource);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM app_metadata", Integer.class)).isEqualTo(1);
    }
}
