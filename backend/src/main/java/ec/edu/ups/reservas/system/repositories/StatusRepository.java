package ec.edu.ups.reservas.system.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StatusRepository {
    private final JdbcTemplate jdbc;

    public StatusRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean isInitialized() {
        return "base-1".equals(jdbc.queryForObject(
                "SELECT value FROM app_metadata WHERE key = ?", String.class, "schema_version"));
    }
}
