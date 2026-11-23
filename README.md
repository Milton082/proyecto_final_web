# Sistema de Gestión de Reservas de Laboratorios Universitarios

Proyecto integrador desarrollado por **dos integrantes**. Esta entrega contiene una base ejecutable de Angular + Spring Boot + SQLite (opción A del PDF). La pantalla inicial verifica la API y la base local. Autenticación, roles, CRUD y reservas están planificados; todavía no están implementados.

## Requisitos previos (Windows 11 / PowerShell)

- Git.
- Node.js **24.x LTS**, versión comprobada 24.15.0, y npm **11.x** (comprobado 11.12.1).
- **JDK 21**, con `java` y `javac` disponibles. Comprobado con Temurin 21.0.11.
- Internet para instalar paquetes y para la primera descarga de Gradle/dependencias Java.

No hace falta instalar Gradle global, SQLite, PostgreSQL ni Docker. El Wrapper descarga Gradle 9.2.0 y el driver JDBC incluye SQLite. PowerShell de Windows puede ejecutar `npm.cmd` si una política local bloquea `npm.ps1`; no es necesario desactivar la política global.

```powershell
git --version
node --version
npm.cmd --version
java -version
javac -version
```

Si Java no se detecta, instalar JDK 21 y configurar `JAVA_HOME` a su carpeta real (sin `bin`) y agregar su `bin` a `PATH`; abrir otra terminal. No versionar rutas propias del equipo.

## Clonar y preparar

El repositorio ya tenía este remoto; se conservó. Desde la carpeta donde se quieran guardar los proyectos:

```powershell
git clone https://github.com/Milton082/proyecto_final_web.git reservas-laboratorios
Set-Location .\reservas-laboratorios
git remote -v
npm.cmd ci
```

Estos pasos descargan la base **después de que su Pull Request se publique e integre en main**. Mientras esté en revisión, ejecutar `git switch --track origin/feature/base-inicial` tras clonar para obtener esta rama. Hasta publicarla, el remoto conserva su contenido anterior.

En el equipo actual la carpeta se llama `reservas-laboratorio` (singular). Eso no cambia los comandos internos. Angular permanece en la raíz y no hay una segunda carpeta `frontend/`.

## Ejecutar en dos terminales

Terminal 1, situada en la raíz del repositorio:

```powershell
Set-Location .\backend
.\gradlew.bat bootRun
```

Al arrancar, Spring Boot crea `backend/data/reservas.db` y ejecuta en este orden:

1. `backend/src/main/resources/db/schema.sql`.
2. `backend/src/main/resources/db/seed.sql`.

Son idempotentes: reiniciar conserva los datos. Por ahora solo crean una tabla y semilla técnicas, sin usuarios. No hay credenciales de prueba hasta implementar autenticación con hashes. Si se cambia el esquema durante el desarrollo, actualizar los scripts de forma explícita; `CREATE TABLE IF NOT EXISTS` no modifica automáticamente tablas existentes.

Terminal 2, situada en la raíz:

```powershell
npm.cmd start
```

Abrir [http://localhost:4200](http://localhost:4200). Debe aparecer **Backend conectado · Base de datos disponible**. Detener cada proceso con `Ctrl+C` en su terminal.

Para comprobar la conexión desde PowerShell, con ambos procesos activos:

```powershell
Invoke-RestMethod http://127.0.0.1:8080/api/status
Invoke-RestMethod http://localhost:4200/api/status
```

Ambas peticiones deben devolver `application=reservas-laboratorios`, `status=UP`, `database=UP`. La segunda atraviesa el proxy de Angular. El endpoint consulta SQLite; no devuelve un éxito fijo.

## Configuración

`src/environments/environment.ts` define `/api`. `proxy.conf.json` redirige `/api/**` a `http://127.0.0.1:8080` durante `npm start`. El navegador no accede directamente a SQLite ni necesita CORS abierto.

El backend usa `backend/src/main/resources/application.yml` y escucha solo localmente. No necesita `.env` ni contraseña de base de datos. Para cambiar el puerto, ejecutar desde `backend/`:

```powershell
$env:PORT = '8081'
.\gradlew.bat bootRun
```

Si se hace ese cambio, actualizar también `target` en `proxy.conf.json` y reiniciar Angular. Para usar otra base, definir `$env:DB_URL = 'jdbc:sqlite:C:/ruta/existente/reservas.db'` antes de arrancar; crear su carpeta previamente. La URL se conserva solo en esa terminal.

El proxy no forma parte del build de producción. Una futura publicación deberá servir `/api` bajo el mismo origen o configurar URL/CORS expresamente. Despliegue remoto fuera del alcance actual.

## Compilar y probar

Desde la raíz:

```powershell
npm.cmd run build
npm.cmd run test:ci
Push-Location .\backend
.\gradlew.bat test bootJar
Pop-Location
```

Resultados reales y alcance: [docs/VERIFICACION_BASE.md](docs/VERIFICACION_BASE.md). Las pruebas de persistencia usan SQLite temporal real. El JAR ejecutable queda en `backend/build/libs/reservas-laboratorios-0.1.0-SNAPSHOT.jar`; para ejecutarlo, usar `java -jar .\build\libs\reservas-laboratorios-0.1.0-SNAPSHOT.jar` desde `backend/`.

## Estructura

```text
src/app/features/    Angular por dominio (pages, services, interfaces)
src/app/core/        Transversal futuro
src/app/shared/      Piezas compartidas futuras
backend/            API Spring Boot por dominio y recursos SQL
backend/data/       Base SQLite local ignorada por Git
docs/               Requisitos, arquitectura, modelo, plan y comprobaciones
```

Documentos: [arquitectura base](docs/ARQUITECTURA_BASE.md), [referencia](docs/ARQUITECTURA_REFERENCIA.md), [decisiones](docs/DECISIONES_TECNICAS.md), [modelo ER conceptual](docs/MODELO_ER.md), [plan para dos integrantes](docs/PLAN_DESARROLLO.md) y [requisitos oficiales](docs/REQUISITOS_PROYECTO.pdf). Las carpetas de dominios futuros son puntos de organización, no módulos implementados.

## Publicar esta base (solo cuando el propietario lo decida)

Se creó la rama local `feature/base-inicial`. `main`, su historial y `origin` se conservaron. No se realizó commit ni push. Ya existen commits en este repositorio: no ejecutar `git init`, no reemplazar `origin`, no usar force push.

Desde la raíz, revisar todos los archivos, incluidos los que estén sin seguimiento:

```powershell
git status --short --branch
git diff --stat
git diff
git remote -v
# Verificar que dependencias, builds, secretos y bases locales se ignoran
git check-ignore node_modules backend/build backend/data/reservas.db .env

# Cuando ambos hayan revisado los cambios y pasado las pruebas:
git add .
git diff --cached --stat
git diff --cached --check
git commit -m "Prepara base Angular Spring Boot y SQLite"
git push -u origin feature/base-inicial
```

Después abrir un Pull Request `feature/base-inicial` → `main` en GitHub, solicitar revisión del compañero e integrar cuando pase la revisión. El push de arriba es una instrucción para el usuario; no fue ejecutado por el asistente. Si Git pide identidad, cada integrante configura su propio `user.name` y `user.email` verificado, sin compartir cuentas.

## Comenzar a trabajar en pareja

El propietario invita al compañero como colaborador. Crear manualmente un GitHub Project Board vinculado al repositorio con `To Do → In Progress → Done`, dar acceso al docente `ctimbi` según el PDF y registrar issues con un único responsable. Las invitaciones y el tablero no se crearon desde esta tarea.

Tras integrar la base, el segundo integrante sigue “Clonar y preparar”, ejecuta las pruebas y arranca ambos proyectos. Cada quien abre sus ramas desde main actualizado:

```powershell
git switch main
git pull --ff-only origin main
git switch -c feature/laboratorios
# Realizar un cambio pequeño, probar y revisar el diff
git add .
git commit -m "Agrega gestión inicial de laboratorios"
git push -u origin feature/laboratorios
```

Integrante 1 comienza con autenticación/usuarios y flujo docente; integrante 2 con laboratorios/equipos/disponibilidad y flujo estudiantil. Ambos usan un único módulo de reservas. Antes de cambiar SQL, contratos, rutas o configuración compartidos, acordar quién edita el archivo. Revisión mutua antes de integrar, con evidencia de pruebas en la PR. Detalle por día y entregables: `docs/PLAN_DESARROLLO.md`.

## Diferencias con el alcance final

El PDF exige SQLite, calendario semanal, reservas/estados/validaciones, notificaciones internas, incidencias, reportes, prototipo, seed con usuarios por rol, informe, tablero y video. Esta base prepara su implementación. El documento menciona 3 integrantes/14 días; el equipo ha solicitado 2 integrantes/10 días. La adaptación se registra en el plan y debe coordinarse con el docente.
