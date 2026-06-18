# SpringThymeleaf-Mvc

Sistema de gestión de reparación (taller/garage) con Spring Boot 4.0.4, Thymeleaf, JPA y PostgreSQL.

## Stack

- Java 21
- Spring Boot 4.0.4
- Thymeleaf
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Lombok
- Maven

## Estructura del proyecto

```
com.arielzarate.Spring_thymeleaf_Mvc/
├── SpringThymeleafMvcApplication.java       # Entry point
├── controller/
│   ├── apiController.java                    # GET /app → api.html
│   └── ClientController.java                 # CRUD /client
├── dto/
│   └── ClientDTO.java                        # id, name, lastName, phone, address, email, cuit
├── persistence/
│   ├── entity/Client.java                    # Entidad JPA (tabla: client)
│   └── repository/ClientRepository.java      # JpaRepository<Client, Long>
├── service/
│   ├── ClientService.java                    # Interfaz del servicio
│   ├── ClientServiceImpl.java                # Implementación con @Transactional
│   └── mapper/ClientMapper.java              # toModel() / toEntity()
```

## Endpoints

Context-path: `/api`

| Método | Ruta            | Descripción            |
|--------|-----------------|------------------------|
| GET    | /app            | Página de bienvenida   |
| GET    | /client         | Listar clientes        |
| GET    | /client/new     | Formulario nuevo       |
| POST   | /client         | Crear cliente          |
| GET    | /client/{id}    | Formulario editar      |
| POST   | /client/{id}    | Actualizar cliente     |
| DELETE | /client/{id}    | Eliminar cliente       |

## Base de datos

- **Motor:** PostgreSQL
- **Host:** localhost:5432
- **Base:** garage_db
- **Usuario:** postgres
- **Tabla:** client
- **Columnas:** id, name, last_name, phone, address, email, cuit
- **DDL:** `ddl-auto: update` (Hibernate gestiona el schema automáticamente)

## Templates Thymeleaf

| Template            | Descripción                                           |
|---------------------|-------------------------------------------------------|
| index.html          | Listado de clientes con tabla y botones Editar/Eliminar |
| formClient.html     | Formulario reutilizable (nuevo cliente / editar)       |
| api.html            | Landing page simple                                   |
| layout/layout.html  | Layout con fragments reutilizables (navbar y footer) |

## Layout con fragments (`th:replace`)

### Estructura del layout

`layout/layout.html` define fragmentos reutilizables con `th:fragment`:

```html
<nav class="navbar" th:fragment="navbar">
    <!-- menú de navegación -->
</nav>

<main class="main-content" th:fragment="content">
    <!-- contenido dinámico (placeholder) -->
</main>

<footer class="footer" th:fragment="footer">
    <!-- pie de página -->
</footer>
```

### Cómo se usa en las páginas

Cada template (`index.html`, `formClient.html`) usa `th:replace` para inyectar los fragmentos del layout:

```html
<!-- index.html / formClient.html -->
<body>
    <nav th:replace="~{layout/layout :: navbar}"></nav>

    <!-- Contenido propio de cada página -->
    <main class="main-content">
        ... contenido específico ...
    </main>

    <footer th:replace="~{layout/layout :: footer}"></footer>
</body>
```

### ¿Cómo funciona `th:replace`?

`th:replace="~{layout/layout :: navbar}"` significa:
1. Buscar el archivo `layout/layout.html` en la carpeta `templates/`
2. Dentro de ese archivo, encontrar el elemento con `th:fragment="navbar"`
3. Reemplazar completamente el elemento actual por ese fragmento

### Ventaja

Si querés cambiar el navbar o el footer (agregar un link, cambiar el texto del copyright), lo hacés **en un solo archivo** (`layout/layout.html`) y se refleja en todas las páginas.

### CSS

Cada página declara sus propios CSS en su propio `<head>`, no hereda nada del layout:

| Página | CSS que carga |
|---|---|
| `index.html` | `layout.css` + `index.css` |
| `formClient.html` | `layout.css` + `formClient.css` |
| `layout/layout.html` | No carga CSS (solo provee fragments) |

## Conceptos Thymeleaf

### `${...}` vs `*{...}` (expresiones)

- **`${client.id}`** — Expresión de variable: busca `id` dentro del objeto `client` (acceso estándar).
- **`*{id}`** — Expresión de selección: busca `id` dentro del objeto definido en `th:object`. Es una abreviatura de `${client.id}` cuando ya estás dentro del contexto del objeto.

En `formClient.html`:
```html
<form th:action="..." th:object="${client}" method="post">
    <input th:field="*{name}">
```
El `th:object="${client}"` declara que todo lo que esté adentro del form usa `*{...}` para referirse a propiedades de `client`.

### El hidden input del ID

```html
<input type="hidden" th:field="*{id}" th:if="${client.id != null}">
```

| Parte | Qué hace |
|-------|----------|
| `type="hidden"` | No se muestra visualmente al usuario |
| `th:field="*{id}"` | Genera automáticamente los atributos `name="id"`, `id="id"` y `value="..."` del input |
| `th:if="${client.id != null}"` | Solo se renderiza si el cliente tiene ID (cuando se está **editando**) |

**Comportamiento:**
- **Nuevo cliente** (`id == null`): el hidden input NO se renderiza → `POST` va a `/client` (crear)
- **Editar cliente** (`id != null`): el hidden input SÍ se renderiza con el ID → `POST` va a `/client/{id}` (actualizar)

Esto permite que el mismo formulario (`formClient.html`) se reutilice tanto para crear como para editar.

### Por qué el controller necesita pasar un `ClientDTO` vacío al form

```java
@GetMapping("/new")
public String showCreateForm(Model model) {
    model.addAttribute("client", new ClientDTO());
    return "formClient";
}
```

El `model.addAttribute("client", new ClientDTO())` es necesario porque el form usa `th:object="${client}"`. Thymeleaf necesita que `${client}` exista en el model para poder renderizar el formulario correctamente. Sin ese objeto, Thymeleaf lanza un error porque `${client}` no está definido.

**Flujo completo del formulario:**
1. `GET /client/new` → controller crea un `ClientDTO()` vacío → lo mete en el model → Thymeleaf renderiza `formClient.html` con inputs vacíos
2. Usuario completa los campos y hace submit a `POST /client`
3. Spring recolecta los datos del form con `@ModelAttribute("client") ClientDTO clientDTO` (vinculación automática por `th:field`)
4. El DTO se guarda en la BD vía `clientService.save(clientDTO)`
5. Lo mismo aplica para editar: `GET /client/5` → el controller pasa el `ClientDTO` con datos → los inputs aparecen precargados

### Validación del formulario con `@Valid` + `th:errors`

#### 1. Dependencia necesaria en pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

`spring-boot-starter-validation` incluye:
- **Jakarta Bean Validation API** (`jakarta.validation.constraints`) — las anotaciones como `@NotEmpty`, `@Email`, etc.
- **Hibernate Validator** — la implementación que ejecuta las validaciones

#### 2. Anotaciones de validación en el DTO

Las validaciones se declaran sobre el **DTO** (`ClientDTO.java`), no sobre la entidad `Client.java`:

```java
public class ClientDTO {
    private Long id;

    @NotEmpty(message = "El nombre es obligatorio")
    private String name;

    @NotEmpty(message = "El apellido es obligatorio")
    private String lastName;

    @NotEmpty(message = "El teléfono es obligatorio")
    private String phone;

    @NotEmpty(message = "La dirección es obligatoria")
    private String address;

    @NotEmpty(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotEmpty(message = "El CUIT es obligatorio")
    private String cuit;
}
```

| Anotación | Qué valida | Viene de |
|---|---|---|
| `@NotEmpty` | Que el campo no esté vacío (ni null, ni "") | `jakarta.validation.constraints` |
| `@Email` | Que tenga formato de email válido (user@dominio.com) | `jakarta.validation.constraints` |
| `@Size(min, max)` | Que el texto tenga entre min y max caracteres | `jakarta.validation.constraints` |
| `@Pattern(regexp)` | Que coincida con una expresión regular | `jakarta.validation.constraints` |
| `@Min` / `@Max` | Valor numérico mínimo / máximo | `jakarta.validation.constraints` |
| `@NotBlank` | Que no sea null, ni vacío, ni solo espacios | `jakarta.validation.constraints` |

El parámetro `message` es el texto que se muestra al usuario cuando la validación falla.

#### 3. El Controller: `@Valid` activa la validación

```java
@PostMapping
public String create(@Valid @ModelAttribute("client") ClientDTO clientDTO, Errors errors) {
    log.info("POST create client: {} {}", clientDTO.getName(), clientDTO.getLastName());

    if (errors.hasErrors()) {
        log.warn("validation errors: {}", errors.getAllErrors());
        return "formClient";  // Vuelve al formulario con los errores
    }

    clientService.save(clientDTO);
    return "redirect:/client";
}
```

##### ¿Cómo funciona `@Valid` paso a paso?

```
El usuario hace submit del formulario
        │
        ▼
POST /client llega al controller
        │
        ▼
Spring ve @Valid en el parámetro clientDTO
        │
        ▼
Spring toma el objeto ClientDTO armado con los datos del form
        │
        ▼
Spring recorre CADA CAMPO del DTO y revisa sus anotaciones:
        │
        ├── @NotEmpty → ¿el valor es null o ""?
        ├── @Email    → ¿el texto tiene formato email?
        ├── @Size     → ¿el largo está dentro del rango?
        └── ...etc
        │
        ├── ✅ Todos pasan → Errors queda vacío → sigue el método
        │
        └── ❌ Algún campo falla → Spring guarda el error en Errors
```

| Elemento | Rol |
|---|---|
| `@Valid` | Activa la inspección de las anotaciones del DTO. Sin esto, Spring ignora los `@NotEmpty`, `@Email`, etc. |
| `Errors errors` | Contenedor donde Spring deposita los resultados de la validación |
| `errors.hasErrors()` | Devuelve `true` si al menos una validación falló |
| `return "formClient"` | Vuelve a mostrar el form, ahora con los errores disponibles para Thymeleaf |

##### Regla fundamental: `Errors` debe ir inmediatamente después de `@Valid`

```java
// ✅ CORRECTO
public String create(@Valid @ModelAttribute("client") ClientDTO clientDTO, Errors errors)

// ❌ INCORRECTO (hay otro parámetro entre @Valid y Errors)
public String create(@Valid @ModelAttribute("client") ClientDTO clientDTO, Model model, Errors errors)

// ❌ INCORRECTO (falta Errors → si hay error, tira HTTP 400)
public String create(@Valid @ModelAttribute("client") ClientDTO clientDTO)
```

Si no se incluye `Errors` (o `BindingResult`) y la validación falla, Spring lanza una excepción `MethodArgumentNotValidException` que resulta en un error HTTP 400, sin mostrar el formulario con los errores.

#### 4. HTML: mostrar los errores con `th:errors`

```html
<form th:action="..." th:object="${client}" method="post">

    <div class="form-group">
        <label for="name">Nombre:</label>
        <input type="text" id="name" th:field="*{name}" placeholder="Nombre">
    </div>
    <span th:if="${#fields.hasErrors('name')}" th:errors="*{name}" class="span-errors">Error name</span>

    <div class="form-group">
        <label for="lastName">Apellido:</label>
        <input type="text" id="lastName" th:field="*{lastName}" placeholder="Apellido">
    </div>
    <span th:if="${#fields.hasErrors('lastName')}" th:errors="*{lastName}" class="span-errors">Error last name</span>

    <!-- ... más campos ... -->

</form>
```

| Atributo Thymeleaf | Qué hace |
|---|---|
| `th:if="${#fields.hasErrors('name')}"` | Solo renderiza el span si hay error en el campo `name`. Sin errores, el span no aparece en el HTML final |
| `th:errors="*{name}"` | Inserta el mensaje de error del campo `name` (el `message` del DTO: "El nombre es obligatorio") |
| `class="span-errors"` | Clase CSS para estilizar el error en color rojo |

##### ¿Qué es `#fields`?

`#fields` es una utilidad que Thymeleaf expone automáticamente cuando el `Errors` está presente en el modelo. Sin el `Errors` en el controller, `#fields` está vacío y los spans nunca se muestran, incluso si el DTO tiene anotaciones.

##### OJO: HTML `required` vs `@Valid`

```html
<!-- ❌ NO USAR ambos juntos -->
<input type="text" th:field="*{name}" required>

<!-- ✅ USAR solo @Valid (sin required en HTML) -->
<input type="text" th:field="*{name}">
```

El `required` del HTML **bloquea el envío del form** en el navegador antes de que llegue al servidor. Si el navegador frena el envío, `@Valid` nunca se ejecuta y los `th:errors` nunca se muestran.

**Decisión de diseño:** usar solo `@Valid` + `th:errors` (sin `required` en HTML) para que toda la validación pase por el servidor y los errores se muestren con estilo.

#### 5. CSS: clase `span-errors`

Definida en `static/css/formClient.css`:

```css
.span-errors {
    color: #dc3545;          /* rojo */
    font-size: 0.8rem;       /* texto más chico que el input */
    display: block;          /* aparece en nueva línea */
    margin: 0.25rem 0 0.5rem 0;
    grid-column: 1 / -1;     /* ocupa todo el ancho en el grid del form */
}
```

#### 6. Flujo completo de validación (diagrama)

```
Usuario completa el form y hace submit
        │
        ▼
POST /api/client (sin required en HTML, el navegador deja pasar)
        │
        ▼
Spring recibe el request
        │
        ▼
Crea un ClientDTO con los datos del formulario
        │
        ▼
@Valid inspecciona cada campo del DTO:
        │
   ┌────┴────────────┬──────────────┬──────────────┐
   ▼                 ▼              ▼              ▼
@NotEmpty name   @NotEmpty lastName  @NotEmpty email   @NotEmpty cuit
   │                 │              @Email email        │
   ▼                 ▼                 ▼                ▼
Errors.hasErrors() ──┴── ¿alguna falló?
        │
        ├── NO → clientService.save(clientDTO) → redirect:/client ✅
        │
        └── SÍ → return "formClient"
                │
                ▼
        Thymeleaf renderiza formClient.html otra vez
                │
                ▼
        Los datos que el usuario puso siguen en los inputs
        (Spring los conserva en el objeto client del model)
                │
                ▼
        <span th:errors="*{name}"> → "El nombre es obligatorio" (en rojo)
        <span th:errors="*{email}"> → "Formato de email inválido" (en rojo)
                │
                ▼
        El usuario ve los errores, corrige y vuelve a enviar
```

#### 7. ¿Por qué validar en el DTO y no en la Entity?

| Razón | Explicación |
|---|---|
| **El DTO es el contrato de entrada** | Define qué espera la API del usuario. Las reglas pueden cambiar sin afectar la BD |
| **La Entity representa la BD** | Es un mapeo de la tabla. Puede reutilizarse en distintos contextos (carga, actualización, etc.) con distintas reglas |
| **`@Valid` actúa sobre el DTO** | El controller valida el `@ModelAttribute`, que es el DTO, no la Entity |
| **Separación de responsabilidades** | La Entity se ocupa de persistencia, el DTO se ocupa de validación de entrada |

#### 8. Resumen de las piezas que intervienen

| Capa | Archivo | Qué hace |
|---|---|---|
| Dependencia | `pom.xml` | `spring-boot-starter-validation` trae Jakarta Validation + Hibernate Validator |
| DTO | `ClientDTO.java` | Declara las anotaciones `@NotEmpty`, `@Email` con sus mensajes de error |
| Controller | `ClientController.java` | Usa `@Valid` para activar validación y `Errors` para capturar resultados |
| HTML | `formClient.html` | Usa `th:errors` y `#fields.hasErrors()` para mostrar errores condicionalmente |
| CSS | `formClient.css` | Clase `.span-errors` para dar estilo rojo a los mensajes de error |

### Listado: cómo se renderiza la tabla de clientes

#### 1. Controller envía los datos

```java
@GetMapping
public String getClients(Model model) {
    model.addAttribute("clients", clientService.findAll());
    return "index";
}
```

`clientService.findAll()` devuelve `List<ClientDTO>`. El controller lo mete en el model con el nombre `clients` y Thymeleaf lo recibe en el HTML.

#### 2. HTML recorre la lista con `th:each`

```html
<tr th:each="client : ${clients}">
    <td th:text="${client.id}"></td>
    <td th:text="${client.name}"></td>
    <td th:text="${client.lastName}"></td>
    <td th:text="${client.phone}"></td>
    <td th:text="${client.address}"></td>
    <td th:text="${client.email}"></td>
    <td th:text="${client.cuit}"></td>
</tr>
```

| Atributo Thymeleaf | Qué hace |
|---|---|
| `th:each="client : ${clients}"` | Itera sobre la lista `clients`. Por cada elemento, crea un `<tr>` y asigna el elemento actual a la variable `client` |
| `th:text="${client.nombre}"` | Inserta el valor de la propiedad como texto plano (escapa HTML automáticamente) |

**¿Qué genera Thymeleaf en el navegador?** Si hay 3 clientes en la BD, el HTML final es:

```html
<tbody>
    <tr>
        <td>1</td>
        <td>Juan</td>
        <td>Pérez</td>
        <td>123456789</td>
        <td>Av. Siempre Viva 123</td>
        <td>juan@email.com</td>
        <td>20-12345678-9</td>
    </tr>
    <tr>
        <td>2</td>
        <td>María</td>
        <td>García</td>
        <td>987654321</td>
        <td>Calle Falsa 456</td>
        <td>maria@email.com</td>
        <td>27-98765432-1</td>
    </tr>
    <tr>
        <td>3</td>
        <td>Carlos</td>
        <td>López</td>
        <td>555555555</td>
        <td>Av. Central 789</td>
        <td>carlos@email.com</td>
        <td>23-55555555-5</td>
    </tr>
</tbody>
```

Cada `th:text="${client.propiedad}"` se reemplaza por el valor real de ese campo.

#### 3. Estado vacío con `th:if`

```html
<tr th:if="${clients == null or clients.empty}">
    <td colspan="8">No hay clientes registrados</td>
</tr>
```

Si no hay clientes (`clients.empty`), se muestra una fila de una sola celda (ocupa 8 columnas con `colspan="8"`) con el mensaje.

### Resumen visual del flujo listado

```
Controller                         index.html (Thymeleaf)           Navegador (HTML final)
───────                            ──────────────────────           ─────────────────────
                                   
clientService.findAll()                                                
        │                                                             
        ▼                                                             
List<ClientDTO> ──────────►   <tr th:each="client : ${clients}">     
                                   │                                  
                                   ▼                                  
                               <td th:text="${client.id}">    ──►  1
                               <td th:text="${client.name}">  ──►  Juan
                                   ...                                ...
                                   
(empty) ─────────────────►   <tr th:if="${clients.empty}">   ──►  "No hay clientes registrados"
```

### Editar: flujo completo desde el botón hasta el formulario precargado

#### 1. El botón "Editar" en la tabla del listado

```html
<a th:href="@{/client/{id}(id=${client.id})}" class="btn btn-sm btn-warning">Editar</a>
```

Genera un link como `/api/client/5` (donde `5` es el ID de ese cliente). Cuando el usuario hace clic, llega al controller.

#### 2. Controller busca el cliente por ID

```java
@GetMapping("/{id}")
public String showEditForm(@PathVariable Long id, Model model) {
    var client = clientService.findById(id);
    if (client.isEmpty()) {
        log.warn("client not found: {}", id);
        return "redirect:/client";
    }
    model.addAttribute("client", client.get());
    return "formClient";
}
```

| Paso | Qué pasa |
|---|---|
| `@GetMapping("/{id}")` | Captura el ID desde la URL (`/api/client/5`) |
| `@PathVariable Long id` | Spring extrae `5` de la URL y lo asigna al parámetro |
| `clientService.findById(id)` | Busca en la BD. Devuelve `Optional<ClientDTO>` |
| `client.isEmpty()` | Si no existe ese ID en la BD... |
| `return "redirect:/client"` | Redirige al listado (`/api/client`). **Está bien puesto** porque Spring MVC interpreta `redirect:/client` como relativo al context-path `/api`, generando `/api/client` |

#### 3. Si el cliente existe: se precarga el formulario

```java
model.addAttribute("client", client.get());
return "formClient";
```

Lo mismo que en creación, pero en vez de `new ClientDTO()` vacío, pasa el `ClientDTO` **con datos** de la BD.

#### 4. Thymeleaf recibe el objeto con datos y los pinta en los inputs

```html
<form th:action="@{'/client/' + ${client.id}}" th:object="${client}" method="post">
    <input type="hidden" th:field="*{id}">
    <input type="text" th:field="*{name}" placeholder="Nombre">
    <input type="text" th:field="*{lastName}" placeholder="Apellido">
    ...
```

Como el `ClientDTO` tiene todos los campos llenos, Thymeleaf genera:

```html
<form action="/api/client/5" method="post">
    <input type="hidden" name="id" value="5">
    <input type="text" name="name" value="Juan" placeholder="Nombre">
    <input type="text" name="lastName" value="Pérez" placeholder="Apellido">
    ...
```

#### Comparativa: Nuevo vs Editar (mismo formulario)

| | **Nuevo** (`/client/new`) | **Editar** (`/client/5`) |
|---|---|---|
| Controller pasa | `new ClientDTO()` | `clientService.findById(5).get()` |
| `th:action` genera | `/api/client` | `/api/client/5` |
| Hidden ID | No se renderiza | `<input type="hidden" name="id" value="5">` |
| Inputs | Vacíos | Precargados con datos de la BD |
| Submit va a | `POST /api/client` (crear) | `POST /api/client/5` (actualizar) |

#### Diagrama visual del flujo editar

```
Botón "Editar" en listado
        │
        ▼   clic en /api/client/5
Controller.showEditForm(5)
        │
        ├── findById(5) → Optional.empty()
        │       │
        │       ▼
        │   redirect:/client  (vuelve al listado)
        │
        └── findById(5) → ClientDTO con datos
                │
                ▼
        model.addAttribute("client", clientDTO)
                │
                ▼
        formClient.html (Thymeleaf)
                │
                ▼
        Inputs precargados: "Juan", "Pérez", etc.
                │
                ▼
        Usuario edita y hace submit → POST /api/client/5 → update
```

### Link URLs con `@{...}` (Link Expressions)

El `@{...}` es la **expresión de link** de Thymeleaf. Sirve para construir URLs de forma dinámica, y automáticamente agrega el **context-path** (`/api`) a la URL.

#### Sintaxis básica

```html
<a th:href="@{/client}">Clientes</a>
<!-- Genera: <a href="/api/client">Clientes</a> -->
```

#### URLs con variables en la ruta (path variables)

```html
<a th:href="@{/client/{id}(id=${client.id})}">Editar</a>
```

| Parte | Significado |
|---|---|
| `@{/client/{id}...}` | Template de URL con un placeholder `{id}` |
| `(id=${client.id})` | Pasa el valor de `${client.id}` al placeholder `{id}` |

**¿Qué genera?** si `client.id = 5`:
```html
<a href="/api/client/5">Editar</a>
```

Es exactamente lo mismo que escribir `${client.id}` con concatenación manual, pero muchísimo más limpio:

```java
// Sin @{} — tendrías que hacer esto a mano:
th:href="${'/client/' + client.id}"
// Con @{} — Thymeleaf lo hace solo:
th:href="@{/client/{id}(id=${client.id})}"
```

#### URLs con query params

```html
<a th:href="@{/client/search(nombre=${client.name}, page=1)}">
<!-- Genera: /api/client/search?nombre=Juan&page=1 -->
```

#### Links estáticos (CSS, JS, imágenes)

```html
<link rel="stylesheet" th:href="@{/css/layout.css}">
<!-- Genera: /api/css/layout.css -->
```

Sin `th:href`, los links estáticos se rompen al usar `context-path` porque el navegador no sabe que debe prefijar `/api`.

#### Diferencia entre los 3 tipos de expresiones

| Expresión | Qué hace | Ejemplo |
|---|---|---|
| `${...}` | Accede a variables del model | `${client.name}` → `"Juan"` |
| `*{...}` | Accede a propiedades del `th:object` actual | `*{name}` → `"Juan"` |
| `@{...}` | Construye URLs (agrega context-path automático) | `@{/client/5}` → `/api/client/5` |

### Eliminar: cómo funciona el DELETE con form + `_method`

#### 1. El HTML en el listado

```html
<form th:action="@{/client/{id}(id=${client.id})}"
      method="post"
      onsubmit="return confirm('¿Estás seguro que querés eliminar este cliente?')">

    <input type="hidden" name="_method" value="delete"/>

    <button type="submit" class="btn btn-sm btn-danger btn-delete">
        Eliminar
    </button>
</form>
```

#### 2. ¿Por qué un `<form>` en vez de un link `<a>`?

Los navegadores HTML **solo soportan GET y POST** en formularios y enlaces. No existe `<a href="..." method="delete">` ni `<form method="delete">` nativo. Para hacer una petición DELETE desde HTML sin JavaScript, se necesita este workaround.

#### 3. ¿Cómo funciona `_method=delete`?

Spring Boot tiene un filtro llamado **`HiddenHttpMethodFilter`** (activado en `application.yml` con `spring.mvc.hiddenmethod.filter.enabled: true`).

El flujo es:

```
Navegador                          Spring Boot
─────────                          ───────────

POST /api/client/5                 HiddenHttpMethodFilter
  _method=delete                      │
    │                                 ▼
    │                             Ve que _method=delete
    │                             → Cambia el POST a DELETE
    │                                 │
    │                                 ▼
    │                             @DeleteMapping("/{id}")
    │                             public String delete(@PathVariable Long id)
```

El navegador envía un POST normal, pero Spring **reenvía internamente** la petición como DELETE al `@DeleteMapping`.

#### 4. El controller

```java
// Eliminar
@DeleteMapping("/{id}")
public String delete(@PathVariable Long id) {
    log.info("request delete client id: {}", id);
    clientService.deleteById(id);
    log.info("client deleted successfully");
    return "redirect:/client";
}
```

| Paso | Qué hace |
|---|---|
| `@DeleteMapping("/{id}")` | Solo se ejecuta si el método HTTP es DELETE (Spring ya lo cambió desde POST gracias al filtro) |
| `@PathVariable Long id` | Toma el `5` de `/api/client/5` |
| `clientService.deleteById(id)` | Llama al service que a su vez llama a `clientRepository.deleteById(id)` → `DELETE FROM client WHERE id = 5` en SQL |
| `return "redirect:/client"` | Vuelve al listado, que ahora ya no tiene ese cliente |

#### 5. El service

```java
@Override
@Transactional
public void deleteById(Long id) {
    log.info("Delete client by id: {}", id);
    clientRepository.deleteById(id);
}
```

`deleteById` es un método provisto por `JpaRepository`. Lanza una excepción si no existe el ID (en ese caso Spring devuelve un error 500).

#### 6. El `confirm()` nativo del navegador

```html
onsubmit="return confirm('¿Estás seguro que querés eliminar este cliente?')"
```

- Cuando el usuario hace clic en "Eliminar", el navegador **no envía el form inmediatamente**
- Primero muestra un cuadro de diálogo con "Aceptar" / "Cancelar"
- Si el usuario hace clic en "Aceptar" → el form se envía (post con _method=delete)
- Si hace clic en "Cancelar" → el form NO se envía

Es un diálogo nativo del navegador, no se puede estilizar. Para tener un modal con estilo, se necesita una librería como SweetAlert2 o Bootstrap modals.

#### 7. Diagrama visual del flujo completo Eliminar

```
Botón "Eliminar" en la tabla
        │
        ▼
  confirm() → ¿Estás seguro?
        │
        ├── "Cancelar" → no pasa nada
        │
        └── "Aceptar"
                │
                ▼
        POST /api/client/5
        Header: _method=delete
                │
                ▼
        HiddenHttpMethodFilter
        (convierte POST → DELETE)
                │
                ▼
        @DeleteMapping("/{id}")
        delete(5)
                │
                ▼
        ClientService.deleteById(5)
                │
                ▼
        ClientRepository.deleteById(5)
                │
                ▼
        SQL: DELETE FROM client WHERE id = 5
                │
                ▼
        redirect:/client → listado sin el cliente
```

#### 8. ¿Por qué esta es la forma estándar en Spring MVC?

- **Sin JavaScript**: funciona 100% con HTML puro
- **Sigue el estándar REST**: usa `@DeleteMapping` en el backend, no un POST normal
- **Profesional**: es la técnica recomendada por Spring para formularios HTML que necesitan métodos HTTP más allá de GET/POST
- **El `confirm()` se puede reemplazar**: por SweetAlert2, Bootstrap modal, o cualquier librería JS sin tocar el backend

La única alternativa sin form es usar JavaScript (fetch o HTMX) para hacer el DELETE directamente.

#### Resumen visual de los enlaces en el listado

```
dato en BD                   th:href con @{}                  HTML final en navegador
──────────                   ────────────────                 ────────────────────────

client.id = 1    →   @{/client/{id}(id=${client.id})}   →   href="/api/client/1"
client.id = 5    →   @{/client/{id}(id=${client.id})}   →   href="/api/client/5"
client.id = 42   →   @{/client/{id}(id=${client.id})}   →   href="/api/client/42"
```

Cada fila de la tabla genera un link de editar que apunta al ID específico de ese cliente.

## CSS

- `layout.css` — Estilos generales (navbar, footer, layout)
- `index.css` — Estilos para la tabla de listado
- `formClient.css` — Estilos para el formulario
- `api.css` — Estilos para la landing page

## Cómo ejecutar

```bash
# Requisitos: Java 21, Maven, PostgreSQL corriendo en localhost:5432

mvn spring-boot:run
```

La aplicación arranca en: http://localhost:8080/api

## Notas

- El layout con fragments (`layout.html`) está creado pero las vistas aún no lo utilizan (tienen navbar/footer hardcodeados).
- La validación con `BindingResult` está comentada en el controller.
- Se usa `_method=delete` con POST para eliminar clientes (Spring HiddenHttpMethodFilter).
- La entidad usa `last_name` (snake_case) y el DTO usa `lastName` (camelCase) — el mapper se encarga de la conversión.

## HTMX — Complemento opcional a Thymeleaf

Sitio oficial: [https://htmx.org](https://htmx.org)

HTMX es una librería JavaScript liviana que **complementa a Thymeleaf** (no lo reemplaza). Permite hacer peticiones AJAX sin escribir JavaScript, usando solo atributos HTML.

### Cómo se relaciona con Thymeleaf

| Tecnología | Rol |
|---|---|
| **Thymeleaf** | Sigue renderizando el HTML del lado del servidor |
| **HTMX** | Intercepta clicks/submits y reemplaza solo fragmentos del DOM sin recargar la página |

### Flujo actual (sin HTMX)

```
[clic Eliminar] → POST /client/5 → Controller → redirect:/client → Recarga TODA la página
```

### Flujo con HTMX

```
[clic Eliminar] → DELETE /client/5 → Controller → responde solo <tr> vacío
                                                  → HTMX reemplaza solo esa fila
                                                  → NO recarga la página
```

### Cómo se integraría en este proyecto

#### 1. Agregar la librería (CDN en el layout o en index.html)

```html
<script src="https://unpkg.com/htmx.org@2.0.4"></script>
```

#### 2. Eliminar con HTMX (sin recargar)

```html
<!-- Actual (con submit y recarga completa) -->
<form th:action="@{/client/{id}(id=${client.id})}" method="post">
    <input type="hidden" name="_method" value="delete"/>
    <button type="submit">Eliminar</button>
</form>

<!-- Con HTMX (borra la fila sin recargar) -->
<button hx-delete="@{/client/{id}(id=${client.id})}"
        hx-target="closest tr"
        hx-swap="outerHTML"
        hx-confirm="¿Estás seguro?">
    Eliminar
</button>
```

#### 3. El controller devuelve solo el fragmento

```java
// En vez de redirect (que recarga todo), devolvés un fragmento vacío
@DeleteMapping("/{id}")
public String delete(@PathVariable Long id) {
    clientService.deleteById(id);
    return ""; // HTMX reemplaza el <tr> por nada → la fila desaparece
}
```

### Atributos principales de HTMX

| Atributo | Qué hace |
|---|---|
| `hx-get`, `hx-post`, `hx-put`, `hx-delete` | Método HTTP a usar |
| `hx-target` | Elemento del DOM a actualizar con la respuesta |
| `hx-swap` | Cómo reemplazar: `innerHTML`, `outerHTML`, `beforeend`, `none` |
| `hx-trigger` | Cuándo ejecutar: `click`, `change`, `submit`, `load` |
| `hx-confirm` | Muestra confirmación antes de enviar |
| `hx-vals` | Enviar valores adicionales (JSON) |

### Ventajas

- **Sin escribir JavaScript**: solo atributos HTML
- **Sin cambiar el backend**: el controller Spring sigue siendo el mismo
- **Sin recargar la página**: mejor experiencia de usuario
- **Compatible con Thymeleaf**: podés usar `${}`, `@{}`, `th:each`, etc. combinados con atributos HTMX
- **Fácil de adoptar**: podés migrar de a una funcionalidad por vez

### Desventajas

- Dependencia de CDN/librería externa
- No es útil si necesitás una SPA compleja con mucho estado del lado del cliente (ahí conviene React/Vue)
- La lógica de fragmentos puede volverse tediosa en formularios muy grandes

Para más información: [https://htmx.org](https://htmx.org)

## Ideas para escalar el sistema (arquitectura general)

### Sistema de autenticación y roles propuesto

#### Roles del sistema

| Rol | Acceso |
|---|---|
| **ADMIN** | Crea usuarios, ve todo (clientes, vehículos, ventas, reparaciones, presupuestos) |
| **MECHANIC** | Solo ve reparaciones asignadas, carga repuestos, fotos, descripciones, tiempo de reparación. No ve clientes, costos ni presupuestos |
| **MANAGER** | Hace presupuestos basados en lo que cargó el mecánico, maneja costos |
| **SALES** | Ve ventas del día, puede cargar nuevas ventas |

#### Login
- Spring Security con `BCryptPasswordEncoder`
- Página de login personalizada (no la default de Spring)
- Solo los ADMIN pueden crear usuarios y asignar roles

#### Protección de rutas por rol

```
/client/**           → solo ADMIN
/repairs/**          → ADMIN, MECHANIC, MANAGER
/repairs/me/**       → MECHANIC (solo sus reparaciones asignadas)
/budgets/**          → ADMIN, MANAGER
/sales/**            → ADMIN, SALES
/users/**            → solo ADMIN
```

#### Dashboard por rol
Cada rol ve su propio dashboard al entrar:
- **ADMIN**: tarjetas con totales (clientes, vehículos en taller, ventas del día, reparaciones activas)
- **MECHANIC**: solo sus reparaciones pendientes con tiempo estimado
- **MANAGER**: reparaciones listas para presupuestar
- **SALES**: ventas del día y métricas

#### Entidades nuevas necesarias

```
User            (email, password, name, role)
Vehicle         (patente, marca, modelo, año, cliente_id)
Repair          (vehiculo_id, mecanico_id, estado, descripcion, fotos, tiempo_estimado)
RepairPart      (repair_id, repuesto, cantidad, precio)
Budget          (repair_id, total, estado, creado_por)
Sale            (vehiculo_id, items, total, vendedor_id)
```

#### Dependencias nuevas a agregar en el pom.xml
- `spring-boot-starter-security`
- `thymeleaf-extras-springsecurity6` (para usar `sec:authorize` en los templates)

### Tecnología complementaria: HTMX
Ver sección HTMX arriba para mejorar la UX sin recargar páginas completas.

---

## Arquitectura de configuración profesional (Settings desde DB)

### Idea general

En lugar de usar `messages.properties` para datos como nombre del taller, año, autor del footer, etc., se usa una **tabla en la BD** para que el admin pueda editarlos desde un panel sin tocar archivos ni reiniciar.

### Flujo de datos

```
┌──────────────────────┐
│  messages.properties │ ← valores por defecto (fallback si no hay en DB)
│  company.name=Taller │
└──────────┬───────────┘
           │
┌──────────▼──────────┐     ┌──────────────────┐
│  DB: system_config  │────►│  ConfigService    │ ← busca primero en DB,
│  key = value        │     │  si no hay →      │   si no existe cae al
└─────────────────────┘     │  fallback al      │   default del properties
                            │  properties       │
                            └────────┬──────────┘
                                     │
                            ┌────────▼──────────┐
                            │ @ControllerAdvice │ ← inyecta en TODAS las
                            │ model.addAttribute│   vistas automáticamente
                            │ ("companyName",   │
                            │  configService.get│
                            │  ("company.name"))│
                            └────────┬──────────┘
                                     │
                                     ▼
                            ┌────────────────────┐
                            │ Thymeleaf templates│
                            │ ${companyName}     │ ← disponible en todas las vistas
                            └────────────────────┘
```

### Componentes necesarios

#### 1. Entidad JPA

```java
@Entity
@Table(name = "system_config")
public class SystemConfig {

    @Id
    private String key;

    private String value;
}
```

#### 2. Repository

```java
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
}
```

#### 3. Service con fallback al properties

```java
@Service
public class ConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Value("${company.name:Taller Mecanico}")
    private String defaultCompanyName;

    public ConfigService(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }

    public String get(String key, String defaultValue) {
        return systemConfigRepository.findById(key)
                .map(SystemConfig::getValue)
                .orElse(defaultValue);
    }
}
```

#### 4. @ControllerAdvice (disponible en todas las vistas)

```java
@ControllerAdvice
public class GlobalConfigAdvice {

    private final ConfigService configService;

    public GlobalConfigAdvice(ConfigService configService) {
        this.configService = configService;
    }

    @ModelAttribute("companyName")
    public String companyName() {
        return configService.get("company.name", "Taller Mecanico");
    }

    @ModelAttribute("footerAuthor")
    public String footerAuthor() {
        return configService.get("footer.author", "Ariel Zarate");
    }

    @ModelAttribute("companyYear")
    public String companyYear() {
        return configService.get("company.year", "2026");
    }
}
```

#### 5. En el HTML (ya no usa `#{}`, usa `${}`)

```html
<footer class="footer" th:fragment="footer">
    <p>&copy; <span th:text="${companyYear}">2026</span>
       <span th:text="${companyName}">Taller</span>.
       Todos los derechos reservados.</p>
    <p>Desarrollado por <span th:text="${footerAuthor}">Ariel Zarate</span></p>
</footer>
```

### Ventajas de este enfoque

- **Admin edita desde un panel** en el frontend sin tocar archivos ni reiniciar el servidor
- **Fallback seguro**: si la DB no tiene el valor, se usa el default del `application.yml`
- **Un solo `@ControllerAdvice`** centralizado, no repetir lógica en cada controller
- **Disponible en todas las vistas** automáticamente, sin pasar el model en cada controller
- **Escalable**: se puede agregar cualquier config sin modificar el HTML (solo agregar la key en DB y el `@ModelAttribute`)

### Desventajas/consideraciones

- Requiere una consulta a DB en cada request (se puede mitigar con caché tipo `@Cacheable`)
- Para datos que cambian poco (nombre del taller, año), `messages.properties` puede ser suficiente

---

## JSP vs Thymeleaf

### JSP (Java Server Pages)
- Tecnología **vieja** de Java para generar HTML dinámico
- Mezcla código Java con HTML: `<%= cliente.getNombre() %>` o `<% if(...) { %>`
- Depende del motor JSP de Tomcat (Jakarta Server Pages)
- Difícil de mantener porque la lógica Java queda incrustada en el HTML
- **Spring Boot ya no lo recomienda** para proyectos nuevos

### Thymeleaf (lo que usa este proyecto)
- Es el **reemplazo moderno y recomendado** por Spring Boot
- Usa atributos HTML en lugar de mezclar Java: `<span th:text="${cliente.nombre}">`
- No contamina el HTML con código Java
- Los templates se pueden abrir en el navegador directamente (el diseñador ve algo aunque no haya servidor)
- Se integra nativamente con Spring Boot (Spring Boot Starter Thymeleaf)

### Diferencia visual

```
JSP:       <p>El cliente se llama <%= cliente.getNombre() %></p>
Thymeleaf: <p th:text="${cliente.nombre}">Nombre del cliente</p>
```

### ¿Thymeleaf reemplaza a JSP?

**Sí, es su sucesor natural.** Hacen lo mismo (generar HTML desde el servidor), pero Thymeleaf es la forma moderna y recomendada por Spring Boot para proyectos nuevos.

## Tecnologías Java para cada tipo de aplicación

### Desktop (escritorio)
- **JavaFX** — Única opción Java moderna para desktop. No está dentro del JDK desde Java 11, se agrega como dependencia aparte. Mantenimiento por OpenJFX/Gluon. Adecuada para apps legacy empresariales.

### Web (backend)
- **Spring Boot** — Estándar de facto en Java para web. IoC, autoconfig, ecosistema enorme.

### SSR (Server-Side Rendering)
- **Thymeleaf** — El reemplazo moderno de JSP. Atributos HTML, sin código Java en la vista. Recomendado por Spring Boot.
- **JSP** — Legado, Spring ya no lo recomienda para proyectos nuevos.

### Fullstack SPA
- **Spring Boot API** + **React** / **Vue** / **Angular** / **Svelte**
- **Híbrido**: Spring Boot + Thymeleaf + HTMX (sin SPA, comportamiento dinámico con atributos HTML)

### Alternativas no-Java para desktop
Si no hay restricción de usar Java, opciones más usadas hoy:
- **Tauri** (Rust + HTML/JS) — Liviano, moderno, creciendo rápido
- **Electron** (JS/TS) — VS Code, Discord, Slack, Spotify
- **Flutter** (Dart) — Mobile + Desktop mismo código
- **.NET MAUI** (C#) — Ecosistema Microsoft, Windows nativo

### Recomendación para este proyecto
Spring Boot + Thymeleaf + HTMX + PostgreSQL es la combinación correcta para un sistema de gestión web como este.


