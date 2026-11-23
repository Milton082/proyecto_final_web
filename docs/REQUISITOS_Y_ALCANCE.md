# Requisitos oficiales y alcance de esta base

Fuente principal: `REQUISITOS_PROYECTO.pdf`, 5 páginas, copia íntegra del PDF aportado. Se leyó todo el texto y se revisaron las páginas renderizadas. Las instrucciones de la guía son requisitos del proyecto académico, no autorización para publicar, invitar colaboradores o ejecutar todas las funcionalidades en esta etapa.

| PDF | Requisito final | Situación en la base / implementación prevista |
| --- | --- | --- |
| p. 2, §1 | Opción A: API REST + SQLite; Angular permitido; acceso a datos separado | Preparado: Angular/HTTP → Spring/servicio/repositorio → SQLite |
| p. 2, §1 | ER mínimo, script de creación, seed con usuarios por rol | ER conceptual completo; SQL y seed técnicos. Tablas de negocio y usuarios con hash: días 4-8 |
| p. 2, §2 | Login local con usuario/contraseña hash y rutas protegidas por rol | Pendiente; integrante 1, día 4 |
| p. 2, §3 | CRUD laboratorios (nombre, ubicación, capacidad, software, foto, estado), equipos y usuarios | Carpetas previstas; días 4-5 según distribución |
| p. 2, §4 | Atención por franjas, bloqueos, calendario semanal por laboratorio | Modelo conceptual y módulo schedules previsto; integrante 2 |
| p. 2, §5 | Docente solicita laboratorio, asignatura/motivo, pendiente y revisión con mensaje | Contrato conceptual; integrante 1, días 6-7 |
| p. 2, §5 | Estudiante reserva puesto/equipo, confirmación automática si hay cupo | Contrato conceptual; integrante 2, días 6-7 |
| p. 2, §5 | Sin solapamientos, capacidad, bloqueos ni fechas pasadas | Reglas documentadas; transacciones y pruebas pendientes |
| p. 2, §5 | Pendiente/Aprobada/Rechazada/Cancelada/Finalizada; finalizar automáticamente | Modelo conceptual; integrante 1, día 7 |
| p. 2, §5 | Cada usuario consulta historial y cancela antes del inicio | Incluye docentes y estudiantes; ambos por flujo |
| p. 3, §6 | Docentes/estudiantes reportan daño con descripción/prioridad; admin actualiza incidencia/equipo | Modelo y módulo incidents; integrante 2, día 8 |
| p. 3, §7 | Bandeja interna: solicitud, aprobación/rechazo, cambios de incidencias | Módulo notifications compartido; integrante 1 coordina contrato |
| p. 3, §8 | Ocupación por laboratorio y equipos con más incidencias | Consultas/reportes previstos; integrante 2 |
| p. 3, §9 | Prototipo navegable UX/UI, responsive, paneles diferenciados por rol | Pendiente del prototipo y módulos. Página de diagnóstico actual no sustituye prototipo final |
| p. 3, §10 | Clonar, instalar, crear BD, seed y ejecutar solo con README | Instrucciones y pruebas base preparadas; publicación pendiente del propietario |
| pp. 1, 3-5 | Project Board, issues con responsable único, commits personales, informe/video/IA | Pasos documentados; tablero, invitaciones y enlaces requieren actuación del equipo en GitHub |

## Diferencias con el planteamiento preliminar

- SQLite pasa de alternativa a decisión requerida por la opción A.
- La referencia PostgreSQL no se traslada al stack nuevo; sí sus capas y convenciones.
- Cancelación/historial también para el docente; las incidencias pueden reportarlas ambos roles.
- Se incorporan explícitamente calendario semanal, finalización automática y datos de laboratorio/equipo detallados.
- Notificaciones internas únicamente; no se exige email/SMS. Reportes limitados a los dos indicados, sin analítica adicional.
- El PDF usa tres personas y catorce días; el usuario indica dos personas y diez días. Se planifica la pareja y se deja registrada la diferencia para coordinarla con el docente.
- La guía exige conocimiento global del proyecto: cada integrante debe revisar y poder explicar módulos ajenos además de los propios.

## Límites de la primera entrega

Solo preparación del repositorio, arranque, HTTP, persistencia técnica, comprobaciones y documentación. Las carpetas reservadas no acreditan funcionalidades. No se generó una implementación completa de entidades, autenticación, reservas, notificaciones o CRUD. Consultar `PLAN_DESARROLLO.md` y `MODELO_ER.md` para el trabajo posterior.
