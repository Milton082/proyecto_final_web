# Plan de desarrollo: dos integrantes, diez días

Disponibilidad: 3-4 h/día por persona; total estimado 60-80 horas-persona. Cada issue tiene un único responsable, criterios de aceptación y revisión del compañero. Ambos implementan frontend y persistencia de sus módulos.

## Responsabilidades

| Integrante 1 | Integrante 2 | Compartido |
| --- | --- | --- |
| Autenticación local, roles, usuarios y administración de usuarios | Laboratorios, equipos, horarios y bloqueos | Modelo ER, contratos y SQL |
| Solicitud docente, aprobación/rechazo, historial/cancelación docente y estados | Disponibilidad, reserva y cancelación estudiantil | Un solo núcleo de reservas y pruebas de conflictos |
| Bandeja y notificaciones de solicitudes/aprobaciones | Incidencias y notificaciones de cambios de incidencias usando el mismo servicio | Revisión mutua, pruebas y documentación |
| Contrato compartido de reservas y actualización a Finalizada | Reportes de ocupación y equipos con incidencias | Prototipo, integración y defensa |

La cancelación docente se agrega porque el PDF permite cancelar a cada usuario antes del inicio. Se separa la propiedad de los archivos comunes de reservas para evitar dos implementaciones incompatibles: integrante 1 mantiene contrato/estados; integrante 2 aporta consultas de disponibilidad y flujo estudiantil mediante PR coordinadas.

## Calendario por días relativos

| Día | Integrante 1 | Integrante 2 | Resultado verificable |
| --- | --- | --- | --- |
| 1 | Requisitos, modelo y contrato de reservas | Requisitos, modelo y boceto navegable | Issues de ambos, tablero To Do/In Progress/Done, ER revisado |
| 2 | Estructura Spring Boot/SQLite | Reutilizar Angular y organización por features | Compilan ambos proyectos; revisión de esta base |
| 3 | Revisar SQL, endpoint y backend en clon limpio | Revisar proxy, interfaz y frontend en clon limpio | Clonar y ejecutar solo con README; integrar PR de base |
| 4 | Login, hash, roles y sesión/guards | CRUD inicial de laboratorios | Usuario por rol en seed con hashes; acceso por rol probado |
| 5 | CRUD de docentes/estudiantes | Equipos, horarios y bloqueos | Formularios/API/persistencia; revisión mutua; prototipo actualizado |
| 6 | Solicitudes docentes y contrato único de reservas | Solicitud estudiantil y calendario semanal sobre disponibilidad | Dos flujos usando misma API/modelo |
| 7 | Aprobación/rechazo con mensaje, cancelación docente, Finalizada | Cupo, no solapamiento, cancelación estudiantil y equipos dañados | Transacciones y pruebas: bloqueos, pasados, solapamientos, capacidad |
| 8 | Notificaciones internas e historial | Incidencias, estados de equipos y dos reportes | Requisitos funcionales del PDF conectados |
| 9 | Pruebas cruzadas por roles, correcciones | Pruebas cruzadas por roles, responsive y correcciones | Demo completa desde instalación limpia; tablero actualizado |
| 10 | Informe, guía, declaración IA y video | Informe, guía, declaración IA y video | Entrega, ER/SQL/seed definitivos y main estable |

El alcance funcional del PDF es amplio para 60-80 h. Mantener implementaciones simples, notificaciones internas sin servicios externos y reportes SQL/tabulares básicos. Si surge retraso, reducir decoración y extras; no declarar opcionales los requisitos oficiales. Actualizar el plan con el docente si un requisito no puede completarse.

## Flujo Git y archivos compartidos

Ramas `feature/autenticacion`, `feature/laboratorios`, `feature/reservas-docentes`, `feature/disponibilidad`, etc. Cada integrante crea sus commits con su propia identidad. Pull Request revisada por el otro antes de integrar a `main`. No trabajar los dos en `schema.sql`, `seed.sql`, `app.routes.ts`, `application.yml`, el contrato de reservas o `package*.json` simultáneamente sin acordarlo.

Al empezar cada jornada: sincronizar `main`, elegir un issue, moverlo a In Progress y crear rama. Al terminar: pruebas, commit pequeño, PR con evidencia y mover a Done solo tras aceptación. No publicar todas las evidencias al final.

## GitHub Projects y entregables externos

Preparar manualmente en GitHub (no creados desde esta tarea): invitar al segundo integrante, vincular un Project Board y dar acceso de lectura al usuario `ctimbi` conforme al PDF. Revisar los permisos disponibles según la visibilidad del repositorio antes de invitarlo. Cada issue tendrá un responsable único de la pareja.

Entregables finales: código e historial, enlace al tablero, ER/script/seed con usuarios por rol, prototipo navegable, README reproducible, informe (proceso, arquitectura, reparto issue/responsable/fecha, guía por rol, IA y retrospectiva individual) y video de 10-12 minutos con exposición de ambos, al menos 3 minutos cada uno. Ambos deben poder explicar y modificar todo el proyecto.

El PDF habla de tres integrantes y catorce días; este plan usa la pareja y diez días indicados por el usuario. Registrar con el docente esa adaptación. La base actual cubre preparación, no acredita que los días 1-3 ni los entregables externos estén completos.
