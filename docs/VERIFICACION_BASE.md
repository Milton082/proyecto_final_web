# Verificación e informe de la base

Fecha de trabajo: 23 de noviembre de 2026. Entorno: Windows 11, PowerShell, Node 24.15.0/npm 11.12.1, JDK Temurin 21.0.11. Los resultados siguientes distinguen comprobaciones ejecutadas de tareas pendientes.

## Hallazgos y cambios

La referencia reúne guías Angular 21 y Spring Boot, con fragmentos sueltos y SQL; no contiene una aplicación ejecutable ni una integración entre ambos. Sus capas y nombres se aplicaron en la base descrita en `ARQUITECTURA_BASE.md`. El PDF exige SQLite para opción A, por eso se adaptó el motor de persistencia.

Se reutilizó el Angular existente en la raíz, sin generar otra aplicación. Se reemplazó su pantalla predeterminada por un diagnóstico de conexión. Se creó `backend/` con Spring Boot/Gradle y SQLite, un endpoint técnico, manejo de errores y pruebas. No se modificó el material de `proyecto-referencia`.

Cambios relevantes sobre archivos existentes:

- `package.json`, `package-lock.json`: nombre, requisitos de entorno y comando de pruebas sin watch; dependencias existentes conservadas.
- `angular.json`, `src/index.html`: nombre del proyecto, idioma español y proxy de desarrollo.
- `src/app/app.*`, `src/styles.css`: shell sencillo, rutas y prueba actualizada.
- `.gitignore`: se retiró la exclusión local de `docs` para compartir documentación; se agregaron Gradle/builds/SQLite/configuración local/secretos.
- `README.md`: reemplaza la plantilla Angular por instalación, ejecución, configuración, Git y trabajo en pareja.

Archivos nuevos por área:

- `.gitattributes`, `AGENTS.md`, `proxy.conf.json`, `src/environments/environment.ts`.
- `src/app/features/system/`: interfaz de estado, servicio HTTP, página y pruebas; `.gitkeep` para dominios futuros/core/shared.
- `backend/build.gradle`, `settings.gradle`, Wrapper completo (incluye JAR/properties/scripts).
- `backend/src/main/java/ec/edu/ups/reservas/`: aplicación, controller/servicio/repositorio/DTO de estado y handler/respuesta de error; dominios futuros reservados.
- `backend/src/main/resources/application.yml`, `db/schema.sql`, `db/seed.sql`, `backend/data/.gitkeep` y `BaseIntegrationTest.java`.
- `docs/REQUISITOS_PROYECTO.pdf` (copia sin cambios del original), `REQUISITOS_Y_ALCANCE.md`, `ARQUITECTURA_BASE.md`, `DECISIONES_TECNICAS.md`, `PLAN_DESARROLLO.md`, `MODELO_ER.md` y este informe. `ARQUITECTURA_REFERENCIA.md` se conserva.

## Comprobaciones

| Comprobación ejecutada | Resultado |
| --- | --- |
| `npm install --package-lock-only --ignore-scripts --offline` | Correcto; lockfile actualizado sin cambiar las versiones de dependencias |
| `npm run build` | Correcto; bundle inicial de producción 220.18 kB, página diferida 2.90 kB |
| `npm.cmd run test:ci` | **5 pruebas pasan en 3 archivos**: shell, contrato HTTP, error y recuperación de la página |
| `.\gradlew.bat test bootJar --no-daemon` desde `backend/` | Correcto; **3 pruebas de integración pasan**, JAR ejecutable generado |
| `.\gradlew.bat bootRun --no-daemon` | Spring Boot 4.0.0 iniciado en 8080; Hibernate 7.1.8.Final y SQLite 3.51.1 |
| `npm.cmd start -- --host 127.0.0.1` | Angular iniciado en 4200 |
| `Invoke-WebRequest http://127.0.0.1:4200 -UseBasicParsing -TimeoutSec 10` | HTTP 200, HTML de la aplicación servido |
| `Invoke-RestMethod http://127.0.0.1:8080/api/status -TimeoutSec 10` | `application=reservas-laboratorios`, `status=UP`, `database=UP` |
| `Invoke-RestMethod http://127.0.0.1:4200/api/status -TimeoutSec 10` y con `localhost` | Mismo resultado atravesando el proxy Angular → Spring → SQLite |
| `git diff --check` | Sin errores de espacios; Git avisa de normalización LF/CRLF en Windows |
| `git check-ignore node_modules backend/build backend/data/reservas.db .env` | Los cuatro objetivos están ignorados; docs y Wrapper JAR no se ignoran |
| Hash SHA-256 del PDF original y copia | Idénticos: `E6CAEE01231E3C3BE71FC633ED4EBAC29179B46487EB540DC35275303D23CF4F` |
| Revisión de archivos candidatos para commit | Sin bases locales, dependencias, builds ni patrones de claves/contraseñas en configuración |

Las pruebas backend levantan servidor HTTP real en puerto aleatorio y una base SQLite temporal. Comprueban contrato `/api/status`, semilla consultada mediante JPA, `PRAGMA foreign_keys=1` y que ejecutar creación/seed dos veces no duplique datos. No cubren CRUD de entidades ni reservas porque no existen aún.

También se exportaron los **82 archivos candidatos a versionarse** a una carpeta temporal fuera del repositorio, sin `.git`, `node_modules`, compilaciones ni SQLite local. Allí `npm.cmd ci --no-audit --no-fund` instaló 470 paquetes, `npm.cmd run build` compiló y `npm.cmd run test:ci` pasó las cinco pruebas. Desde su `backend/`, `.\gradlew.bat test bootJar --no-daemon` recompiló todo y pasó las tres pruebas SQLite con una base temporal nueva. Es una simulación de directorio limpio; no se presenta como un clon de GitHub, porque la rama todavía no se publicó. Las descargas y cachés de herramientas se realizaron en el mismo equipo, no en una segunda computadora.

Limitaciones del entorno de ejecución: Gradle falló inicialmente al cargar su DLL nativa dentro del aislamiento; las ejecuciones autorizadas fuera de él pasaron. La primera comprobación HTTP de Angular también encontró restricciones de sockets del aislamiento y se repitió correctamente fuera. Un intento de instalación npm offline en la exportación falló por paquetes ausentes en caché; la instalación normal autorizada se completó. Ninguna de esas comprobaciones fallidas se contabiliza como una prueba aprobada.

## Estado de Git y tareas del equipo

Repositorio existente con commits `71953e8` y `27b0d96` antes de esta tarea, rama main y remoto `https://github.com/Milton082/proyecto_final_web.git`. Se creó `feature/base-inicial` conservando los cambios locales. No se alteró origin, no se hicieron commits, push, PR, invitaciones ni cambios externos.

El propietario debe revisar, hacer el commit/push y abrir la PR con los comandos del README. El segundo integrante debe ser invitado, clonar la rama publicada, ejecutar `npm ci`, las comprobaciones y ambos servidores. Tras integrar, cada quien abre su rama desde main actualizado.

Pendientes de la etapa funcional: autenticación/roles, datos y usuarios con hashes, CRUD, reglas y flujos de reservas, calendario, incidencias, notificaciones y reportes. Pendientes organizativos: GitHub Project, colaboradores/acceso docente, prototipo y coordinación sobre dos integrantes frente a tres en el PDF. No se confunden con funcionalidades ya implementadas.
