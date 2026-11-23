-- Idempotente: reiniciar la aplicación no borra ni duplica datos.
INSERT OR IGNORE INTO app_metadata (key, value) VALUES ('schema_version', 'base-1');
-- Usuarios por rol y hashes: pendientes del módulo de autenticación.
