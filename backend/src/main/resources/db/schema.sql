-- Base técnica inicial. Las tablas del dominio se incorporarán por módulos.
CREATE TABLE IF NOT EXISTS app_metadata (
    key TEXT PRIMARY KEY,
    value TEXT NOT NULL
);
