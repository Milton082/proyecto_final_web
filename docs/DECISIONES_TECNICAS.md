# Decisiones técnicas

## Fuentes y prioridad

1. `REQUISITOS_PROYECTO.pdf`, copia del archivo proporcionado por el usuario, leído completo (5 páginas).
2. Solicitud del equipo: dos integrantes, 10 días, 3-4 horas diarias por persona y Spring Boot permitido.
3. Guías y fragmentos de `proyecto-referencia`, contrastados con `ARQUITECTURA_REFERENCIA.md`.

El PDF describe 3 integrantes y 14 días; el plan se adapta a los 2 integrantes y 10 días indicados por el equipo. Deben coordinar esa diferencia con el docente. No se presume una tercera persona ni una autorización que no se aportó.

## Stack fijado

| Tecnología | Versión elegida/resuelta | Motivo |
| --- | --- | --- |
| Node.js | 24.15.0 LTS usado en la verificación; engine 24.x | Entorno instalado y compatible con Angular 21 |
| npm | 11.12.1 | Gestor existente; instalación reproducible con `npm ci` |
| Angular core | 21.2.26 en lockfile | Reutiliza el proyecto existente y los patrones Angular 21 |
| Angular CLI/build | 21.2.30 en lockfile | Se conserva la herramienta existente |
| TypeScript / RxJS | 5.9.3 / 7.8.2 en lockfile | Se conservan las dependencias del proyecto |
| Vitest | 4.1.12 en lockfile | Pruebas incluidas en Angular; no añadir otro runner |
| Java | JDK 21 LTS (Temurin 21.0.11 en este equipo) | Ya instalado; toolchain fija Java 21 |
| Spring Boot | 4.0.0 | Versión explícita de la referencia; base educativa, no se presenta como la última versión |
| Gradle Wrapper | 9.2.0 | Compatible con Java 21/Boot 4; disponible en este equipo y distribuible con Wrapper |
| Hibernate ORM / community dialects | 7.1.8.Final, gestionados juntos por el BOM de Boot 4.0.0 | Evitar mezclar versiones del núcleo y del dialecto SQLite |
| SQLite JDBC Xerial | 3.51.1.0 | Driver embebido; no requiere instalar un servidor de base de datos |

`package-lock.json` se versiona. Gradle usa versiones fijas y el BOM de Spring Boot, sin plugin adicional de dependency-management. Las dependencias de backend son Web MVC, Data JPA, Validation, dialectos comunitarios, SQLite JDBC y Spring Boot Test/JUnit. No se añaden Lombok, Docker, Firebase, Tailwind, DaisyUI, Swagger ni Spring Security antes de necesitarlos.

## SQLite frente a PostgreSQL

El PDF p. 2 define **Opción A = API REST + SQLite** y menciona expresamente Spring Boot. PostgreSQL aparece en la referencia docente, pero no satisface la opción A elegida. Se adapta el motor, conservando la separación Controller/Service/Repository/Entity/DTO/Mapper.

SQLiteDialect pertenece a `hibernate-community-dialects`; no recibe las mismas garantías ni pruebas del equipo de Hibernate que PostgreSQL. Se añade esa dependencia con la misma versión del ORM y se prueba contra SQLite real. SQLite tiene limitaciones de ALTER TABLE y escrituras concurrentes; para un servidor local académico se usará una sola instancia, transacciones cortas y scripts explícitos. Los controles de cupo/solapamiento deberán comprobar y guardar dentro de la misma transacción, con pruebas de concurrencia antes de considerarlos completos.

## Simplificaciones de esta etapa

- Angular queda en la raíz del repositorio existente. Crear `frontend/` exigiría mover configuración y dependencias sin beneficio para esta base.
- Se conserva el nombre físico `reservas-laboratorio`; nombre de aplicación y paquetes npm/Gradle: `reservas-laboratorios`. No se cambia el remoto GitHub existente.
- Un módulo técnico `system` comprueba toda la ruta HTTP y la BD. No hay CRUD, login, roles, guards, JWT, usuarios semilla ni reglas de reservas implementados aún.
- SQL crea una tabla técnica y semilla idempotente. El modelo conceptual completo se documenta sin implementar todas sus entidades.
- Autenticación futura: usuarios locales con hash, autorización del backend y guards de Angular; adaptar las guías Spring Security. Firebase no cumple la autenticación local pedida. La estrategia concreta de sesión/JWT se decide en el módulo de autenticación.
- URL relativa `/api` con proxy de desarrollo evita dependencias de la IP de cada integrante. No hace falta `.env` para arrancar; Spring lee variables de PowerShell directamente.
- No se duplica la API de reservas por rol. Docentes y estudiantes comparten reglas y almacenamiento.

## Requisitos que ajustan el alcance preliminar

- Cancelación e historial corresponden a **cada usuario**, también al docente (p. 2).
- Estudiante reserva un puesto o equipo, no todo el laboratorio; confirmación automática bajo cupo y disponibilidad (p. 2).
- Se necesitan calendario semanal y transición automática Aprobada → Finalizada (p. 2).
- Docentes y estudiantes reportan incidencias de equipos; administrador actualiza incidencia y estado del equipo (p. 3).
- Notificaciones son internas. No se exige correo, SMS o servicios externos.
- Reportes mínimos: ocupación por laboratorio y equipos con más incidencias.
- Prototipo navegable, tablero GitHub Projects, video 10-12 minutos, informe y declaración de IA son entregables, aunque no sean funcionalidades del código.

## Referencias técnicas verificadas

- [Requisitos de Spring Boot 4.0](https://docs.spring.io/spring-boot/4.0/system-requirements.html): compatibilidad Java/Gradle; la página de la línea 4.0 puede mostrar un parche posterior.
- [Dialectos Hibernate](https://docs.hibernate.org/orm/7.1/dialect/): SQLite como dialecto comunitario.
- [SQLite JDBC](https://github.com/xerial/sqlite-jdbc): driver embebido.
- [Checksums de Gradle](https://gradle.org/release-checksums/): distribución y Wrapper 9.2.0.

La comprobación real de esta combinación y sus limitaciones se registra en `VERIFICACION_BASE.md`.
