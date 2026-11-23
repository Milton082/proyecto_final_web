# Reglas del proyecto

- El objetivo actual es una base clonable. No implementar módulos completos sin una tarea que lo solicite.
- Leer `docs/REQUISITOS_PROYECTO.pdf`, `docs/ARQUITECTURA_REFERENCIA.md`, `docs/ARQUITECTURA_BASE.md` y `docs/DECISIONES_TECNICAS.md` antes de cambiar arquitectura o persistencia.
- Conservar la arquitectura por dominio de la referencia. No modificar `../proyecto-referencia/`.
- Angular permanece en la raíz (`src/`); Spring Boot en `backend/`. No generar proyectos anidados ni otra aplicación Angular.
- Frontend: standalone, `inject()`, rutas `*.routes.ts`, páginas con `.ts/.html/.css` juntos, servicios HTTP tipados, interfaces simples. Archivos en kebab-case; clases PascalCase; variables y métodos camelCase.
- Backend: paquete `ec.edu.ups.reservas`, dominios con `controllers`, `services`, `repositories`, `entities`, `dtos`, `mappers` según necesidad. Reglas en servicios, consultas en repositorios y errores transversales en `core/exceptions`.
- Mantener un único módulo `reservations` para docentes y estudiantes. Coordinar cambios en contratos, SQL, seguridad, rutas y configuraciones antes de editarlos entre ambos.
- SQLite es obligatorio para la opción A. Conservar scripts SQL explícitos e idempotentes y llaves foráneas activadas. No sustituirlo por PostgreSQL o H2 en pruebas de persistencia.
- Evitar cambios innecesarios y código duplicado. No crear clases vacías o abstracciones genéricas sin uso. Crear las capas de cada dominio al implementar la primera funcionalidad.
- Mantener cambios pequeños y comprobables. Cada integrante trabaja en frontend y persistencia de sus módulos, con su propia identidad Git.
- `main` estable; trabajo en ramas `feature/...` y revisión del otro integrante antes de integrar. No hacer push, cambiar remotos ni modificar accesos de GitHub sin autorización explícita.
- No versionar bases locales, dependencias, builds, `.env`, claves ni configuraciones personales. Nunca almacenar contraseñas de usuarios en texto plano; el seed con hashes se realizará con autenticación.
- Verificación mínima cuando se modifica código: `npm run build`, `npm run test:ci` y, desde `backend/`, `.\gradlew.bat test bootJar`. Para integración, comprobar `/api/status` a través de Angular y del backend.
- No afirmar que una prueba pasó si no se ejecutó. Registrar resultado real, alcance y limitaciones en `docs/VERIFICACION_BASE.md`.
- Documentar dependencias nuevas y mantener README reproducible en PowerShell para Windows 11. No instalar tecnologías o frameworks adicionales sin necesidad.
