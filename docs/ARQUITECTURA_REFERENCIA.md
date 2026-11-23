# Arquitectura de referencia para `reservas-laboratorios`

## Alcance y evidencia

El repositorio `proyecto-referencia` **no es una aplicación única ejecutable**. Es un material didáctico organizado en guías y fragmentos independientes de Angular y Spring Boot. Por tanto, este documento separa los patrones demostrados de las recomendaciones de las guías; no presupone archivos, dependencias ni implementaciones que no estén presentes.

No se encontró un backend Node.js/Express, ni archivos como `package.json`, `server.js`, rutas Express o configuraciones de ORM para Node. La referencia de backend aplicable es **Spring Boot con Gradle, JPA/Hibernate y PostgreSQL**.

> Nota de ubicación: en el espacio de trabajo existe `reservas-laboratorio/` (singular), no `reservas-laboratorios/` (plural). Este informe se deja en la carpeta existente.

## Estructura general de la referencia

```text
proyecto-referencia/
├── angular/
│   ├── 01-instalacion-configuracion/
│   ├── 02-fundamentos-angular/
│   ├── 03-Navegacion/
│   ├── 04-estilos-layout-tailwind/
│   ├── 05-formularios-reactivos/
│   ├── 06-temas-componentes-ui/
│   ├── 07-consumo-servicios-http/
│   ├── 08-firebase-autenticacion/
│   ├── 09-guards-seguridad-rutas/
│   ├── 11-deploy/
│   ├── A1-ui-heuristicas/, A2-ui-heuristicas-impl/, A3-naming/
│   └── docs/
└── spring-boot/
    ├── 01_configuracion.md … 16_docker_ubuntu_server.md
    ├── files/                       # scripts SQL de ejemplo
    ├── assets/
    └── eva/
```

Cada módulo contiene documentación y, en algunos casos, una carpeta `files/` con ejemplos aislados. No existe un árbol fuente completo equivalente a `src/app` o `src/main/java` dentro de la referencia.

## Tecnologías y dependencias relevantes

| Área | Evidencia en la referencia | Aplicación sugerida |
| --- | --- | --- |
| Frontend | Angular 21, TypeScript, componentes standalone, Router, Signals y RxJS | Base del frontend. El proyecto destino ya usa Angular `21.2.x` y RxJS `7.8.x`. |
| Formularios | `@angular/forms`, `FormBuilder`, `ReactiveFormsModule`, validadores síncronos y asíncronos | Formularios de reserva, laboratorio y autenticación. |
| HTTP | `HttpClient`, `provideHttpClient`, Observables, `catchError`, `rxResource`, `toSignal` | Clientes tipados por dominio para la API Spring Boot. |
| UI | Tailwind CSS; en ejemplos posteriores, DaisyUI | Adaptar solo si se instala y se configura explícitamente. No está instalado aún en el proyecto destino. |
| Backend | Spring Boot 4.0.0, Java 17, Gradle (Groovy DSL) y Spring Web | Referencia principal para el backend, en lugar de Express. |
| Persistencia | Spring Data JPA, Hibernate y driver PostgreSQL | API REST con PostgreSQL. La referencia documenta `spring-boot-starter-data-jpa` y `org.postgresql:postgresql`. |
| Seguridad | Spring Security y JJWT `0.12.6`; JWT, roles y refresh tokens | Incorporar cuando exista el módulo de autenticación, no en el esqueleto inicial. |
| Documentación API | Springdoc OpenAPI UI `3.0.3` para Spring Boot 4 | Añadir al estabilizar los endpoints. |
| Firebase | `@angular/fire` y Firebase aparecen en prácticas alternativas | No reutilizar junto con JWT/Spring Boot salvo que se decida una arquitectura Firebase independiente. |

Las versiones de Tailwind, DaisyUI, Spring Web/DevTools y de la mayoría de dependencias de Spring no están fijadas por un archivo de build real en el repositorio de referencia; no deben inferirse.

## Patrones de frontend que sí aparecen

### Organización recomendada

La guía de nomenclatura propone arquitectura por *feature*, co-localizando la vista (`.ts`, `.html`, `.css`) y separando las responsabilidades internas solo cuando sean necesarias:

```text
src/app/
├── core/                         # configuración, autenticación transversal, HTTP global
├── shared/                       # componentes o utilidades realmente comunes
└── features/
    └── reservations/             # o laboratories, auth, users
        ├── reservations.routes.ts
        ├── pages/
        ├── components/
        ├── services/
        ├── data-access/          # opcional: repositorios/adaptadores HTTP
        ├── interfaces/
        ├── validators/
        └── utils/
```

La estructura anterior es una adaptación directa de la recomendación de la guía; no existe como código consolidado en la referencia. Para `reservas-laboratorios`, conviene empezar más simple: `pages`, `services` e `interfaces` dentro de cada feature, y crear `data-access`, `validators` o `components` solo cuando un caso real lo requiera.

### Componentes, rutas y estado

- Componentes `standalone`, con dependencias declaradas en `imports`; no se usan `NgModule` ni `AppModule`.
- Rutas centralizadas en `app.routes.ts`, y rutas de dominio en archivos `*.routes.ts` cuando el módulo crezca.
- Navegación declarativa con `routerLink`, `routerLinkActive`, `RouterOutlet`; parámetros con `ActivatedRoute` y navegación imperativa puntual con `Router`.
- Inyección funcional con `inject()` tanto en componentes como en servicios y guards.
- Estado local con `signal`; datos derivados o asíncronos en los ejemplos HTTP con `toSignal` y `rxResource`.
- Templates modernos con `@if`, `@for` y `@switch`; los listados usan `track` por identificador.
- `ChangeDetectionStrategy.OnPush` aparece en la página de listado HTTP y es consistente con signals; aplicarlo a páginas de datos nuevas.

### HTTP, interfaces y manejo de estados

El patrón demostrado concentra URL, petición, tipos, transformación y error en un servicio `@Injectable({ providedIn: 'root' })`. El componente solo solicita datos y presenta carga, error, vacío o resultado.

- Las interfaces describen tanto el recurso como la envoltura de respuesta y las opciones de consulta.
- Los métodos HTTP devuelven `Observable<T>` tipados.
- `map`, `tap` y `catchError` se usan dentro del servicio; el ejemplo de cache es específico de Simpsons y no es una obligación arquitectónica.
- La paginación de frontend lee `page` de query parameters en un servicio reutilizable y hace que el recurso se recargue al cambiarla.
- La guía recomienda mover la URL base a `environment` cuando haya entornos, aunque el ejemplo inicial aún la deja como constante privada.

Para reservas, usar interfaces separadas y consistentes, por ejemplo para recurso, creación/actualización y respuesta paginada. No copiar los nombres ni el contrato de la API de Simpsons.

### Formularios y validación

Los formularios son reactivos: `FormBuilder`, `FormGroup`, `Validators`, `[formGroup]`, `formControlName` y `ngSubmit`.

- Se marca todo como tocado ante un envío inválido (`markAllAsTouched`).
- Las reglas reutilizables se extraen como funciones `*.validator.ts`: existe un ejemplo de confirmación de contraseña y otro asíncrono de correo único.
- Los mensajes se condicionan a estado de validación y se muestran junto al campo.
- La guía separa la lógica del formulario en TypeScript de la presentación con clases Tailwind.

Para reglas críticas (disponibilidad, cruces de horario, permisos), la validación Angular es solo de experiencia de usuario; el backend debe ser la fuente de verdad.

### Autenticación y protección de rutas

El material Angular implementa esta parte con Firebase, no contra una API JWT propia:

- `AuthService` encapsula login, registro y logout; los componentes no llaman a Firebase directamente.
- El usuario actual se expone como signal con `toSignal(authState(...))`.
- Guards funcionales `authGuard` y `guestGuard` retornan `true` o un `UrlTree` creado por Router.
- El menú refleja el mismo estado que las rutas privadas.
- Hay prácticas de roles con Firebase custom claims, pero no una integración Angular + Spring Security/JWT terminada.

Si se adopta el backend Spring Boot de esta referencia, conservar la separación `AuthService` + guards, pero adaptar su fuente de sesión a tokens y datos de usuario de la API. No reutilizar Firebase ni sus reglas Firestore en paralelo.

## Patrones de backend que sí aparecen

### Organización por dominio

La referencia propone MVCS y módulos por recurso, no una única carpeta global por tipo:

```text
src/main/java/<paquete-raiz>/
├── core/
│   ├── exceptions/
│   └── dtos/                     # reutilizables, p. ej. paginación
├── config/
├── security/                     # cuando sea necesaria
├── laboratories/
│   ├── controllers/
│   ├── services/
│   ├── repositories/
│   ├── entities/
│   ├── dtos/
│   └── mappers/
└── reservations/
    ├── controllers/
    ├── services/
    ├── repositories/
    ├── entities/
    ├── dtos/
    └── mappers/
```

En las guías aparecen variaciones singulares (`repository`, `dto`, `entity`) y plurales (`repositories`, `dtos`, `entities`). Para el proyecto nuevo se debe elegir una sola variante; se recomienda el plural, porque es el que utiliza la estructura modular y de seguridad más completa.

El flujo presentado es: `HTTP → Controller → Service → Repository → JPA/Hibernate → PostgreSQL`. El controlador expone el contrato REST; el servicio contiene reglas y orquestación; el repositorio consulta; los mappers convierten entre DTO, modelo de dominio y entidad.

### CRUD, persistencia y relaciones

- Endpoints REST usan `GET`, `POST`, `PUT`, `PATCH` y `DELETE`.
- Los DTO de entrada y respuesta no exponen directamente entidades JPA.
- `JpaRepository<Entity, Long>` ofrece CRUD y consultas derivadas por convención; los servicios transforman entidades y DTO mediante mappers.
- Las entidades usan JPA/Hibernate y una `BaseEntity` de auditoría propuesta. La referencia también contempla eliminación lógica.
- Para relaciones usa entidades, llaves foráneas y `@ManyToOne`; recomienda `LAZY` como opción a preferir y DTOs de respuesta para evitar exponer el grafo JPA.
- La paginación se modela con un DTO reutilizable de query params (`page`, `size`, `sortBy`, `direction`), validado con `@Valid` y consumido como `Pageable`, `Page` o `Slice`.

En reservas, las entidades, relaciones y reglas de disponibilidad no están presentes en el repositorio y deberán definirse desde el dominio; la referencia solo aporta el modo de organizarlas.

### Validación, errores y seguridad

- DTOs validados con Bean Validation y `@Valid`; las reglas de negocio (unicidad, estado de una operación) permanecen en el servicio.
- Excepciones de dominio `NotFoundException`, `ConflictException` y `BadRequestException` derivan de una excepción base con estado HTTP.
- Un `@RestControllerAdvice` devuelve un único `ErrorResponse` con `timestamp`, `status`, `error`, `message`, `path` y detalles por campo cuando aplican.
- La seguridad propuesta usa Spring Security, filtro JWT, `SecurityConfig`, `UserDetailsService`, roles persistidos y `@PreAuthorize` para autorización por endpoint.
- La guía también distingue autorización por rol de *ownership*: usuario normal puede operar sus recursos; ADMIN puede operar los de cualquier usuario.
- Se documentan access token, refresh token rotativo y logout/revocación. Es una ampliación posterior, no un requisito mínimo de un CRUD inicial.

No existen en la referencia middlewares de Express ni controladores/servicios Node.js. En Spring, los equivalentes son filtros de seguridad, configuración de Spring Security, interceptores cuando correspondan, y `@RestControllerAdvice` para errores.

## Convenciones de desarrollo

- Nombres de archivos y carpetas frontend en `kebab-case`.
- Páginas, componentes y layouts sin el sufijo redundante `.component`: `reservation-form.ts`, `reservation-form.html`, `reservation-form.css`.
- Servicios con `.service.ts`; repositorios con `.repository.ts`; guards con `.guard.ts`; validadores descriptivos con `.validator.ts`; rutas en `*.routes.ts`.
- Interfaces con nombres de contenido simples (`reservation.ts`, `reservation-response.ts`) y no con `.interface.ts` o `.model.ts` en la arquitectura nueva recomendada.
- Una responsabilidad por archivo; no crear archivos genéricos `utils.ts`, `common.ts` o `helpers.ts`.
- En backend, mantener nombres de clases en singular por recurso (`ReservationController`, `ReservationService`, `ReservationEntity`, `ReservationMapper`) y paquetes por dominio. La referencia separa interfaz y clase de implementación de servicio (`Service`/`ServiceImpl`).
- Las respuestas HTTP deben ser consistentes; errores de validación y negocio no deben construirse manualmente en cada controlador.

## Archivos representativos para consultar al implementar

| Necesidad | Archivo de referencia | Qué consultar |
| --- | --- | --- |
| Organización Angular moderna | `proyecto-referencia/angular/A3-naming/A3-naming.md` | Feature folders, sufijos y migración a standalone. |
| Rutas y parámetros | `proyecto-referencia/angular/03-Navegacion/03-Navegacion.md` | `Routes`, Router, `ActivatedRoute`, `routerLink`. |
| Formulario validado | `proyecto-referencia/angular/05-formularios-reactivos/files/5b-p1.ts` y `5b-p4.html` | Formulario reactivo, validación y envío. |
| Validadores reutilizables | `proyecto-referencia/angular/05-formularios-reactivos/files/password-match.validator.ts` | Forma funcional de un validador custom. |
| Cliente HTTP tipado | `proyecto-referencia/angular/07-consumo-servicios-http/files/simpsons.service.ts` | Servicio HTTP, opciones y errores; adaptar imports y nombres. |
| Carga y estados de una página | `proyecto-referencia/angular/07-consumo-servicios-http/files/simpsons-page.ts` y `simpsons-page.html` | `rxResource`, paginación y estados de UI. |
| Guards funcionales | `proyecto-referencia/angular/09-guards-seguridad-rutas/09-guards-seguridad-rutas.md` | `authGuard`, `guestGuard` y `UrlTree`. |
| Arquitectura Spring | `proyecto-referencia/spring-boot/02_estructura_proyecto.md` | Paquetes por dominio y flujo de capas. |
| CRUD, DTO y mapper | `proyecto-referencia/spring-boot/03_api_rest.md` y `04_servicios.md` | Responsabilidad de Controller/Service/DTO/Mapper. |
| JPA y PostgreSQL | `proyecto-referencia/spring-boot/05_repositorios_persistencia.md` | Entidad, repositorio, configuración y mapeo. |
| Errores uniformes | `proyecto-referencia/spring-boot/07_control_errores.md` | Excepciones de dominio y handler global. |
| Relaciones y paginación | `proyecto-referencia/spring-boot/08_relacion_entidades.md` y `10_paginacion.md` | Relaciones JPA, DTOs y `Pageable`. |
| JWT, roles y propiedad | `proyecto-referencia/spring-boot/11_autenticacion_autorizacion.md`, `12_roles_preauthorize.md`, `13_ownership_validacion.md` | Seguridad por capas al llegar a esa etapa. |

## Qué conservar, adaptar y no reutilizar

### Conservar

- Angular standalone, `inject()`, Signals/RxJS, rutas declarativas y formularios reactivos.
- Servicios HTTP tipados y separación clara entre datos, vista y reglas de negocio.
- Backend Spring Boot modular por dominio, DTO/mapper/entity/repository/service/controller.
- Validación en DTO + servicio, errores normalizados, paginación validada y seguridad del lado del servidor.
- Nivel de implementación progresivo: primero CRUD y validación; luego relaciones, paginación, roles y refresh tokens cuando el caso lo exija.

### Adaptar

- Los recursos de ejemplo (`users`, `products`, `categories`, Simpsons) por el dominio real: laboratorios, reservas, usuarios y, solo si es necesario, horarios/equipamiento.
- La URL de API mediante entornos del frontend; no dejar una URL de desarrollo fija cuando haya backend real.
- La autenticación Angular para consumir JWT de Spring Boot, sin asumir que el ejemplo Firebase es compatible tal cual.
- El contrato de paginación de Spring y las interfaces Angular para que coincidan en un único formato antes de implementar pantallas de lista.
- Tailwind/DaisyUI solo tras decidir la dependencia y una guía visual mínima; los ejemplos son de interfaz educativa, no un sistema de diseño obligatorio.

### No reutilizar o no asumir

- Node.js/Express: no existe una implementación de referencia para replicar.
- Firebase/Firestore: es una alternativa didáctica; no combinarla con la persistencia PostgreSQL/JPA y JWT propuesta para este proyecto sin una decisión explícita.
- URL, datos, cache de `localStorage`, imágenes y contrato de The Simpsons API.
- Credenciales de PostgreSQL, secretos JWT y `ddl-auto: update` de los ejemplos. Deben configurarse de forma segura por entorno; los secretos no se versionan.
- El uso de Spring Boot 4.0.0 como versión obligatoria: está documentado por el curso, pero no hay proyecto ejecutable que demuestre compatibilidad completa de todas las dependencias. Confirmar las versiones al crear el backend.

## Funcionalidades ausentes de la referencia

No se encontró una implementación real de:

- Dominio de reservas, comprobación de solapamientos, disponibilidad por horario, cancelaciones o auditoría específica de reservas.
- Integración Angular con el backend Spring Security/JWT documentado.
- Backend Node.js/Express.
- Proyecto monorrepo, CI/CD, pruebas de integración/end-to-end, migraciones versionadas de base de datos o control de concurrencia de reservas.
- Un archivo de dependencias (`package.json`, `pom.xml` o `build.gradle`) de la aplicación de referencia que permita fijar versiones completas.

Estos puntos deberán diseñarse en la siguiente etapa; no deben inventarse a partir de los ejemplos didácticos.
