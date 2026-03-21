# Documentación Completa del Proyecto SpringData

## 1. Resumen Ejecutivo

El proyecto **springdata** es una aplicación Java Spring Boot que implementa un sistema de gestión de clientes y productos con arquitectura hexagonal (Ports & Adapters).
Utiliza Spring Data JPA para persistencia y expone una API REST con documentación Swagger/OpenAPI.

---

## 2. Información General del Proyecto

| Atributo | Valor |
|----------|-------|
| **Nombre** | springdata |
| **Versión** | 1.0-SNAPSHOT |
| **Grupo** | com.arielzarate |
| **Framework** | Spring Boot 3.5.6 |
| **Java Version** | 21 |
| **Build Tool** | Maven |

---

## 3. Estructura de Directorios

```
springdata/
├── src/
│   ├── main/
│   │   ├── java/com/arielzarate/
│   │   │   ├── Application.java                          # Clase principal
│   │   │   ├── application/                               # Capa de Aplicación (Use Cases)
│   │   │   │   ├── ClientUseCase.java
│   │   │   │   └── ProductUseCase.java
│   │   │   ├── domain/                                    # Capa de Dominio
│   │   │   │   ├── model/                                 # Entidades del dominio
│   │   │   │   │   ├── Client.java
│   │   │   │   │   ├── Product.java
│   │   │   │   │   ├── Order.java
│   │   │   │   │   ├── Address.java
│   │   │   │   │   └── enums/
│   │   │   │   │       └── OrderStatus.java
│   │   │   │   ├── ports/                                 # Interfaces (Ports)
│   │   │   │   │   ├── in/
│   │   │   │   │   │   ├── ClientService.java
│   │   │   │   │   │   └── ProductService.java
│   │   │   │   │   └── out/
│   │   │   │   │       ├── ClientPort.java
│   │   │   │   │       └── ProductPersistencePort.java
│   │   │   │   └── services/
│   │   │   │       ├── ClientDomainService.java          # Lógica de negocio de Client
│   │   │   │       └── ProductDomainService.java
│   │   │   ├── infraestructure/                           # Capa de Infraestructura
│   │   │   │   ├── adapter/                               # Implementación de Ports (Adapters)
│   │   │   │   │   ├── ClientAdapter.java
│   │   │   │   │   ├── ProductAdapter.java
│   │   │   │   │   └── mapper/
│   │   │   │   │       ├── ClientEntityMapper.java        # Domain <-> Entity
│   │   │   │   │       └── ProductEntityMapper.java
│   │   │   │   ├── persistence/                           # Persistencia JPA
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── BaseEntity.java               # Base con timestamps, softDelete, softActive
│   │   │   │   │   │   ├── ClientEntity.java
│   │   │   │   │   │   ├── ProductEntity.java
│   │   │   │   │   │   ├── OrderEntity.java
│   │   │   │   │   │   └── AddressEntity.java
│   │   │   │   │   └── repositories/
│   │   │   │   │       ├── ClientRepository.java
│   │   │   │   │       └── ProductRepository.java
│   │   │   │   └── rest/                                  # Controladores REST
│   │   │   │       ├── ClientController.java
│   │   │   │       ├── ProductController.java
│   │   │   │       ├── dto/request/
│   │   │   │       │   ├── ClientRequest.java
│   │   │   │       │   ├── ProductRequest.java
│   │   │   │       │   └── AddressRequest.java
│   │   │   │       ├── dto/response/
│   │   │   │       │   ├── ClientResponse.java
│   │   │   │       │   ├── ProductResponse.java
│   │   │   │       │   └── AddressResponse.java
│   │   │   │       └── mapper/
│   │   │   │           ├── ClientMapper.java               # Domain <-> DTO (REST)
│   │   │   │           ├── ProductMapper.java
│   │   │   │           └── AddressMapper.java
│   │   │   └── error/                                     # Manejo de errores
│   │   │       ├── handlerException.java
│   │   │       └── model/
│   │   │           ├── ApplicationError.java
│   │   │           ├── ClientError.java
│   │   │           └── exception/
│   │   │               ├── ApplicationException.java
│   │   │               └── ClientException.java
│   │   └── resources/
│   │       └── application.yml                           # Configuración
├── pom.xml                                               # Dependencias Maven
└── target/                                               # Compilados
```

---

## 4. Arquitectura del Proyecto

### 4.1 Patrón: Arquitectura Hexagonal (Ports & Adapters)

El proyecto implementa **Arquitectura Hexagonal** con las siguientes capas:

```
+-------------------------------------------------------------+
|                    CAPA DE INTERFAZ (REST)                 |
|  +---------------------+  +-------------------------------+ |
|  |  ClientController   |  |     ProductController        | |
|  |  - GET/POST/PUT/   |  |     - GET/POST/PUT/DELETE   | |
|  |    DELETE /clients |  |       /products              | |
|  |  - PATCH /activate |  |                             | |
|  +----------+----------+  +---------------+-------------+ |
|             |                            |                 |
|  +----------+----------+  +--------------+-------------+  |
|  |    DTOs y Mappers   |  |    DTOs y Mappers           |  |
|  +----------+----------+  +--------------+-------------+  |
+---------------------+---------------------------------------+
                      |                            |
                      v                            v
+-------------------------------------------------------------+
|                    CAPA DE APLICACIÓN                       |
|  +---------------------+  +-------------------------------+ |
|  |    ClientUseCase    |  |       ProductUseCase         | |
|  |  - createClient()   |  |   - getAllProducts()         | |
|  |  - getClientById()  |  |   - getProductById()        | |
|  |  - updateClient()   |  |   - createProduct()         | |
|  |  - deleteClient()   |  |   - updateProduct()         | |
|  |  - getAllClients()  |  |   - deleteProduct()         | |
|  |  - activate()       |  |                             | |
|  +----------+----------+  +---------------+-------------+  |
+---------------------+---------------------------------------+
                      |                            |
                      v                            v
+-------------------------------------------------------------+
|                    CAPA DE DOMINIO                          |
|  +--------------------------------------------------------+ |
|  |                    PORTS (Interfaces)                   | |
|  |  +------------------+          +--------------------+ | |
|  |  |  ClientService   |          |   ProductService  | | |
|  |  |  (Port In)      |          |   (Port In)       | | |
|  |  +--------+---------+          +---------+--------+ | |
|  |           |                                |          | |
|  |  +--------+---------+          +---------+--------+ | |
|  |  |   ClientPort     |          |ProductPersistence | | |
|  |  |   (Port Out)    |          |Port (Port Out)    | | |
|  |  +--------+---------+          +---------+--------+ | |
|  +------------------+-----------------------------+-----+ |
|                     |                               |       |
|  +------------------+-----------------------------+-----+ |
|  |                    DOMAIN MODELS               |       |
|  |  +---------+  +---------+  +---------+  +-------+  |  |
|  |  | Client  |  | Product |  |  Order  |  |Address |  |  |
|  |  | (active)|  |         |  |         |  |        |  |  |
|  |  +---------+  +---------+  +---------+  +-------+  |  |
|  |  +-------------------------------------------------+ |  |
|  |  |            ClientDomainService                  | |  |
|  |  |  - validateForCreate()                         | |  |
|  |  |  - validateForUpdate()                        | |  |
|  |  |  - validateEmail()                           | |  |
|  |  |  - activateClient()                         | |  |
|  |  +-------------------------------------------------+ |  |
|  +------------------------------------------------------+ |
+---------------------------+--------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                    CAPA DE INFRAESTRUCTURA                  |
|  +--------------------------------------------------------+ |
|  |                      ADAPTERS                          | |
|  |  +------------------+          +--------------------+ | |
|  |  |  ClientAdapter   |          |   ProductAdapter   | | |
|  |  | (implementa      |          |  (implementa       | | |
|  |  |  ClientPort)     |          |  ProductPersistence| | |
|  |  |  - softDelete    |          |  Port)             | | |
|  |  |  - softActivate  |          |                    | | |
|  |  +--------+---------+          +---------+--------+ | |
|  +------------------+-----------------------------+-----+ |
|                     |                               |       |
|  +------------------+-----------------------------+-----+ |
|  |                  PERSISTENCE (JPA)            |       |
|  |  +---------------------+  +-------------------+ |       |
|  |  | ClientRepository    |  |   ProductRepository| |    |
|  |  | - findByIdWithAddr |  |   (JpaRepository) | |    |
|  |  | - existsByEmail    |  |                   | |    |
|  |  +---------------------+  +-------------------+ |       |
|  |                                                      | |
|  |  +----------------------------------------------+  |   |
|  |  |              ENTIDADES JPA                    |  |   |
|  |  |  ClientEntity  ProductEntity  OrderEntity   |  |   |
|  |  |  AddressEntity  BaseEntity (timestamps,    |  |   |
|  |  |  softDelete, softActive, isActive)        |  |   |
|  |  +----------------------------------------------+  |   |
|  +------------------------------------------------------+ |
+-------------------------------------------------------------+
```
+-------------------------------------------------------------+
|                    CAPA DE INTERFAZ (REST)                 |
|  +---------------------+  +-------------------------------+ |
|  |  ClientController   |  |     ProductController        | |
|  |  - GET/POST/PUT/    |  |     - GET/POST/PUT/DELETE   | |
|  |    DELETE /clients  |  |       /products             | |
|  +----------+----------+  +---------------+-------------+ |
|             |                            |                 |
|  +----------+----------+  +--------------+-------------+  |
|  |    DTOs y Mappers   |  |    DTOs y Mappers (MapStruct)|  |
|  +----------+----------+  +--------------+-------------+  |
+---------------------+---------------------------------------+
                      |                            |
                      v                            v
+-------------------------------------------------------------+
|                    CAPA DE APLICACIÓN                       |
|  +---------------------+  +-------------------------------+ |
|  |    ClientUseCase    |  |       ProductUseCase         | |
|  |  - createClient()   |  |   - getAllProducts()        | |
|  |  - getClientById()  |  |   - getProductById()        | |
|  |  - updateClient()   |  |   - createProduct()         | |
|  |  - deleteClient()   |  |   - updateProduct()         | |
|  |  - getAllClients()  |  |   - deleteProduct()         | |
|  +----------+----------+  +---------------+-------------+  |
+---------------------+---------------------------------------+
                      |                            |
                      v                            v
+-------------------------------------------------------------+
|                    CAPA DE DOMINIO                          |
|  +--------------------------------------------------------+ |
|  |                    PORTS (Interfaces)                 | |
|  |  +------------------+          +--------------------+ | |
|  |  |  ClientService   |          |   ProductService  | | |
|  |  |  (Port In)       |          |   (Port In)       | | |
|  |  +--------+---------+          +---------+--------+ | |
|  |           |                                |          | |
|  |  +--------+---------+          +---------+--------+ | |
|  |  |   ClientPort     |          |ProductPersistence | | |
|  |  |   (Port Out)     |          |Port (Port Out)    | | |
|  |  +--------+---------+          +---------+--------+ | |
|  +------------------+-----------------------------+-----+ |
|                     |                               |       |
|  +------------------+-----------------------------+-----+ |
|  |                    DOMAIN MODELS               |       |
|  |  +---------+  +---------+  +---------+  +-------+  |  |
|  |  | Client  |  | Product |  |  Order  |  |Address |  |  |
|  |  +---------+  +---------+  +---------+  +-------+  |  |
|  |  +-------------------------------------------------+ |  |
|  |  |            ProductDomainService                 | |  |
|  |  |  - getAll(), getById(), create(), update()     | |  |
|  |  +-------------------------------------------------+ |  |
|  +------------------------------------------------------+ |
+---------------------------+--------------------------------+
                            |
                            v
+-------------------------------------------------------------+
|                    CAPA DE INFRAESTRUCTURA                  |
|  +--------------------------------------------------------+ |
|  |                      ADAPTERS                          | |
|  |  +------------------+          +--------------------+ | |
|  |  |  ClientAdapter   |          |   ProductAdapter   | | |
|  |  | (implementa      |          |  (implementa       | | |
|  |  |  ClientPort)     |          |  ProductPersistence| | |
|  |  |                  |          |  Port)              | | |
|  |  +--------+---------+          +---------+--------+ | |
|  +------------------+-----------------------------+-----+ |
|                     |                               |       |
|  +------------------+-----------------------------+-----+ |
|  |                  PERSISTENCE (JPA)            |       |
|  |  +---------------------+  +-------------------+ |       |
|  |  | ClientRepository    |  |   ProductRepository| |    |
|  |  | (JpaRepository)     |  |   (JpaRepository) | |    |
|  |  +---------------------+  +-------------------+ |       |
|  |                                                      | |
|  |  +----------------------------------------------+  |   |
|  |  |              ENTIDADES JPA                  |  |   |
|  |  |  ClientEntity  ProductEntity  OrderEntity  |  |   |
|  |  |  AddressEntity  BaseEntity (con timestamps, |  |   |
|  |  |                  soft delete, isActive)    |  |   |
|  |  +----------------------------------------------+  |   |
|  +------------------------------------------------------+ |
+-------------------------------------------------------------+
```

---

## 5. Dependencias del pom.xml

### 5.1 Dependencias Principales

| Dependencia | Versión | Propósito |
|-------------|---------|-----------|
| **spring-boot-starter-web** | 3.5.6 | REST APIs con Tomcat embebido |
| **spring-boot-starter-data-jpa** | 3.5.6 | Spring Data JPA + Hibernate |
| **spring-boot-starter-test** | 3.5.6 | Testing con JUnit 5 |
| **lombok** | (desde parent) | Reducción de boilerplate |
| **mapstruct** | 1.6.3 | Mapper de objetos |
| **mapstruct-processor** | 1.6.3 | Procesador de MapStruct |
| **mysql-connector-j** | 8.3.0 | Driver MySQL |
| **slf4j-api** | 2.0.17 | Logging |
| **swagger-annotations** | 2.2.39 | Documentación API |
| **springdoc-openapi-starter-webmvc-ui** | 2.8.10 | Swagger UI |

### 5.2 Plugins Maven

| Plugin | Configuración |
|--------|---------------|
| **spring-boot-maven-plugin** | Genera JAR ejecutable |
| **maven-compiler-plugin** | Compila con Java 21 |

---

## 6. Configuración (application.yml)

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/orders
    username: root
    password: admin
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 30000
      pool-name: HikariPool
      max-lifetime: 600000
      connection-timeout: 30000
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQLDialect

springdoc:
  api-docs:
    path: /docs
  swagger-ui:
    path: /swagger
```

**Endpoints disponibles:**
- API REST: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger`
- Documentación OpenAPI: `http://localhost:8080/api/docs`

---

## 7. Modelos de Dominio

### 7.1 Client (Entidad de Dominio)
```java
@Getter @Setter
public class Client {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean active;
    private Address address;
}
```
**Propósito:** Representa un cliente. No hereda de Person. Incluye campo `active` para soft delete.
**Nota:** Relación con Orders es unidireccional (Order -> Client), no al revés.

---

### 7.2 Address (Entidad de Dominio)
```java
@Data
public class Address {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
```
**Propósito:** Dirección asociada a un cliente (relación OneToOne).

---

### 7.3 Product (Entidad de Dominio)
```java
@Getter @Setter
public class Product {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Boolean isActive;
}
```
**Propósito:** Representa un producto del catálogo.

---

### 7.4 Order (Entidad de Dominio)
```java
@Getter @Setter
public class Order {
    private Long orderId;
    private Client client;
    private List<Product> products;
    private LocalDateTime orderDate;
    private OrderStatus status;
}
```
**Propósito:** Representa un pedido. Relación ManyToMany con Product.

---

### 7.2 Client (Entidad de Dominio)
```java
@EqualsAndHashCode(callSuper = true)
@Getter @Setter
public class Client extends Person {
    private Long clientId;
    private Address address;
    private List<Order> orders = List.of();
}
```
**Propósito:** Representa un cliente que hereda de Person. Incluye relación con Orders.

---

### 7.3 Product (Entidad de Dominio)
```java
@Getter @Setter
public class Product {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Boolean isActive;
}
```
**Propósito:** Representa un producto del catálogo.

---

### 7.4 Order (Entidad de Dominio)
```java
@Getter @Setter
public class Order {
    private Long orderId;
    private Client client;
    private List<Product> products;
    private LocalDateTime orderDate;
    private OrderStatus status;
}
```
**Propósito:** Representa un pedido realizado por un cliente.

---

### 7.5 Address (Value Object)
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
```
**Propósito:** Value Object que representa una dirección.

---

### 7.6 OrderStatus (Enumeración)
```java
public enum OrderStatus {
    CREATED,
    PROCESSING,
    COMPLETED,
    CANCELLED
}
```
**Propósito:** Define los estados posibles de un pedido.

---

## 8. Entidades JPA (Infraestructura)

### 8.1 BaseEntity (Entidad Base)
```java
@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime deletedAt;
    
    private Boolean isActive = true;

    public void softDelete() {
        if (this.isActive) {
            this.isActive = false;
            this.deletedAt = LocalDateTime.now();
        }
    }

    public void softActive() {
        if (!this.isActive) {
            this.isActive = true;
            this.deletedAt = null;
        }
    }
}
```
**Características:**
- Implementa **soft delete** (eliminación lógica)
- Implementa **soft active** (activación lógica)
- Timestamps automáticos (createdAt, updatedAt)
- Campo isActive para control de estado
- Herencia con `@MappedSuperclass`
- Todos los campos tienen Column names en snake_case

---

### 8.2 ClientEntity
```java
@Entity
@Table(name = "client")
public class ClientEntity extends BaseEntity {
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "phone")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
```
**Relaciones:**
- `@OneToOne` con AddressEntity (cascade ALL)

---

### 8.3 ProductEntity
```java
@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private List<OrderEntity> orders;
}
```
**Relaciones:**
- `@ManyToMany` con OrderEntity (lado inverso)

---

### 8.4 OrderEntity
```java
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity clientOrders;

    private LocalDateTime orderDate;

    @ManyToMany
    @JoinTable(
        name = "order_product",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> products;

    private String status;
}
```
**Relaciones:**
- `@ManyToOne` con ClientEntity
- `@ManyToMany` con ProductEntity (lado propietario)

---

### 8.5 AddressEntity
```java
@Entity
@Table(name = "address")
public class AddressEntity extends BaseEntity {
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "country")
    private String country;
}
```

---

## 9. Ports (Interfaces del Dominio)

### 9.1 Ports de Entrada (In)

#### ClientService (Interface)
```java
public interface ClientService {
    public Client createClient(Client client);
    public Client getClientById(Long clientId);
    public void deleteClient(Long clientId);
    public Client updateClient(Client client, Long clientId);
    public List<Client> getAllClients();
}
```

#### ProductService (Interface)
```java
public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
```

### 9.2 Ports de Salida (Out)

#### ClientPort (Interface)
```java
public interface ClientPort {
    public Client saveClient(Client client);
    public Optional<Client> getClientById(Long clientId);
    public void deleteClient(Long clientId);
    public Client updateClient(Client client);
    public boolean existsById(Long clientId);
    public List<Client> getAllClients();
}
```

#### ProductPersistencePort (Interface)
```java
public interface ProductPersistencePort {
    Product saveProduct(Product product);
    Optional<Product> findProductById(Long id);
    void deleteProduct(Long id);
    List<Product> findAllProducts();
    Product updateProduct(Product product);
    Boolean existsProductByName(String name);
    Optional<Product> findProductByName(String name);
    Boolean existsProductByNameAndIdNot(String name, Long id);
}
```

---

## 10. Implementaciones (Adapters)

### 10.1 ClientAdapter
```java
@Component
@AllArgsConstructor
public class ClientAdapter implements ClientPort {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    // Implementa todos los métodos de ClientPort
    // Usa ClientRepository para acceso a datos
    // Convierte entre Client (dominio) y ClientEntity (persistencia)
}
```

### 10.2 ProductAdapter
```java
@Component
@AllArgsConstructor
public class ProductAdapter implements ProductPersistencePort {
    private ProductRepository productRepository;
    private ProductEntityMapper mapper;

    // Implementa todos los métodos de ProductPersistencePort
    // Usa ProductRepository para acceso a datos
    // Convierte entre Product (dominio) y ProductEntity (persistencia)
}
```

---

## 11. Casos de Uso (Application Layer)

### 11.1 ClientUseCase
```java
@Service
@AllArgsConstructor
public class ClientUseCase implements ClientService {
    private final ClientPort clientPort;

    // Delegado: createClient(), getClientById(), updateClient(), getAllClients()
    // Manejo de errores: ClientException
}
```

### 11.2 ProductUseCase
```java
@Service
@AllArgsConstructor
public class ProductUseCase implements ProductService {
    private final ProductDomainService productDomainService;

    // Delega al ProductDomainService
    // NO tiene lógica de negocio (como indica la arquitectura hexagonal)
}
```

---

## 12. Servicios de Dominio

### ClientDomainService
```java
@Service
@AllArgsConstructor
public class ClientDomainService {
    private final ClientPort clientPort;

    // Métodos principales:
    // - validateForCreate(client): valida campos obligatorios, trim, formato email
    // - validateForUpdate(existing, new): merge de datos, mantiene existentes si vienen null
    // - validateEmail(email): valida formato con regex
    // - createClient(client): crea nuevo cliente
    // - updateClient(client, id): actualiza cliente con merge
    // - deleteClient(id): elimina lógicamente (soft delete)
    // - activateClient(id): reactiva cliente lógicamente
    // - getClientById(id): obtiene cliente por id
    // - getAllClients(): obtiene todos los clientes
}
```
**Propósito:** Contains all business logic for Client entity.
**Validaciones:**
- Campos obligatorios en create: firstName, lastName, email, phone
- Email validation con regex
- Email uniqueness check
- Merge logic en update (mantiene valores existentes si vienen null/empty)

---

### ProductDomainService
```java
@Slf4j
@Service
@AllArgsConstructor
public class ProductDomainService {
    private ProductPersistencePort productPersistencePort;

    // Lógica de negocio aquí:
    // - Validaciones (nombre único)
    // - Trim de nombres
    // - Manejo de errores (ApplicationException)
}
```

**Nota importante:** El código incluye un comentario indicando que la lógica de negocio debe estar en la capa de dominio, no en los Use Cases.

---

## 13. Controladores REST

### 13.1 ClientController
```
Endpoints:
- POST   /api/clients              -> createClient() [con validaciones]
- GET    /api/clients              -> getAllClients() [SIN address, CON active]
- GET    /api/clients/{id}         -> getClientById() [CON address, CON active]
- PUT    /api/clients/{id}         -> updateClient() [con merge, sin modificar active]
- DELETE /api/clients/{id}         -> deleteClient() [soft delete - active=false]
- PATCH  /api/clients/{id}/activate -> activateClient() [soft active - active=true]
```

### 13.2 ProductController
```
Endpoints:
- GET    /api/products              -> getAllProducts()
- GET    /api/products/{id}         -> getProductById()
- POST   /api/products              -> createProduct()
- PUT    /api/products/{id}         -> updateProduct()
- DELETE /api/products/{id}         -> deleteProduct()

Anotaciones Swagger:
- @Operation y @ApiResponse para documentación
```

---

## 14. DTOs (Data Transfer Objects)

### Request DTOs
- **ClientRequest**: firstName, lastName, email, phone, street, city, state, postalCode, country
  - Campos de address flatten (no es objeto anidado)
  - Todos los campos son opcionales (se validan en el domain service)
- **ProductRequest**: name, description, price, stock
- **AddressRequest**: street, city, state, postalCode, country

### Response DTOs
- **ClientResponse**: clientId, firstName, lastName, email, phone, active, street, city, state, zipCode, country
  - Incluye campo `active` (Boolean) para indicar estado
  - Campos de address flatten
- **ProductResponse**: productId, name, description, price, stock, isActive
- **AddressResponse**: addressId, street, city, state, zipCode, country

---

## 15. Mappers

### 15.1 Mappers de Persistencia (Manual)
- **ClientEntityMapper**: Convierte Client (domain) <-> ClientEntity (JPA)
  - mapToEntity(): domain -> entity
  - mapToDomainBasic(): entity -> domain (sin address, para getAll)
  - mapToDomainWithAddresses(): entity -> domain (con address, para getById)
- **ProductEntityMapper**: Product (domain) <-> ProductEntity (JPA)

### 15.2 Mappers REST (Manual)
- **ClientMapper**: Client (domain) <-> ClientRequest/ClientResponse
  - mapToDomain(): request -> domain (ignora id, mapea address si viene)
  - mapToResponse(): domain -> response (CON address)
  - mapToClientResponseWithoutAddress(): domain -> response (SIN address, para getAll)
- **ProductMapper**: Product <-> ProductRequest <-> ProductResponse
- **AddressMapper**: Address <-> AddressRequest <-> AddressResponse

---

## 16. Manejo de Errores

### 16.1 Excepciones Personalizadas

#### ApplicationException
```java
public class ApplicationException extends RuntimeException {
    private final ApplicationError error;
}
```

#### ClientException
```java
public class ClientException extends RuntimeException {
    private final ClientError error;
}
```

### 16.2 Errores Predefinidos

#### ApplicationError
- `serverError(String detail)` -> 500 Internal Server Error
- `badRequest(String detail)` -> 400 Bad Request
- `conflict(String detail)` -> 409 Conflict
- `notFound(String field)` -> 404 Not Found

#### ClientError
- `clientNotFound(String field)` -> 404
- `clientConflict(String detail)` -> 409
- `clientBadRequest(String detail)` -> 400
- `serverError()` -> 500

### 16.3 Handler Global
```java
@ControllerAdvice
public class handlerException {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationError> handleApplicationException(...)
    
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ClientError> handleClientException(...)
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationError> genericException(...)
}
```

---

## 17. Repositorios (Spring Data JPA)

### 17.1 ClientRepository
```java
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    @Query("""
        SELECT DISTINCT c
        FROM ClientEntity c 
        LEFT JOIN FETCH c.address
        WHERE c.id = :id
        """)
    Optional<ClientEntity> findByIdWithAddresses(@Param("id") Long id);
}
```
**Características:**
- Hereda de JpaRepository
- Query personalizada con JOIN FETCH para cargar dirección

### 17.2 ProductRepository
```java
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name, Long id);
}
```

---

## 18. Flujo de Datos

### Ejemplo: Crear un Producto

```
1. Cliente envía POST /api/products con ProductRequest
       |
       v
2. ProductController.createProduct() recibe ProductRequest
       |
       v
3. ProductMapper.mapToDomain() convierte a Product (dominio)
       |
       v
4. ProductUseCase.createProduct() delega al domain service
       |
       v
5. ProductDomainService.create() valida y procesa lógica de negocio
       |
       v
6. ProductAdapter.saveProduct() convierte a ProductEntity
       |
       v
7. ProductRepository.save() persiste en MySQL
       |
       v
8. Respuesta fluye inversamente hasta ProductResponse
```

---

## 19. Relaciones de Base de Datos

```
+-----------------+       +-----------------+
|     client      |       |     address     |
+-----------------+       +-----------------+
| id (PK)         |<------| id (PK)         |
| first_name      |  1:1  | street          |
| last_name       |       | city            |
| email           |       | state           |
| phone           |       | postal_code     |
| address_id (FK) |       | country         |
+-----------------+       | created_at      |
        |                 | updated_at      |
        | 1:N             +-----------------+
        v
+-----------------+       +-----------------+     +-----------------+
|     orders      |       |   order_product  |     |     product     |
+-----------------+       +-----------------+     +-----------------+
| id (PK)         |<------| order_id (FK)   |     | id (PK)         |
| client_id (FK)  |  N:M  | product_id (FK)  |---->| name            |
| order_date      |       +-----------------+     | description     |
| status          |                                | price           |
| created_at      |                                | stock           |
| updated_at      |                                | is_active       |
+-----------------+                                +-----------------+
```

---

## 20. Características Notables

### 20.1 Soft Delete (Eliminación Lógica)
- Implementado en BaseEntity con `isActive` y `deletedAt`
- No elimina registros físicamente
- Endpoint DELETE /clients/{id} -> soft delete (active=false, deletedAt=now)
- Endpoint PATCH /clients/{id}/activate -> soft activate (active=true, deletedAt=null)

### 20.2 Soft Activate (Activación Lógica)
- Implementado en BaseEntity con método `softActive()`
- Reactiva clientes eliminados lógicamente

### 20.3 Timestamps Automáticos
- `@CreationTimestamp` -> createdAt
- `@UpdateTimestamp` -> updatedAt

### 20.4 Optimización de Consultas
- **getAllClients()**: NO carga direcciones (evita N+1 queries)
- **getClientById()**: SÍ carga direcciones ( JOIN FETCH)
- Mappers separados: mapToDomainBasic vs mapToDomainWithAddresses

### 20.5 Validaciones en Dominio
- Cliente:
  - Campos obligatorios (create): firstName, lastName, email, phone
  - Formato email (regex)
  - Email único (existe en BD)
  - Trim automático de datos
  - Merge en update (mantiene valores existentes si vienen null/empty)
- Producto: nombre único

### 20.6 Merge en Update
- Los campos que vienen vacíos en el request mantienen los valores existentes
- Solo se actualizan los campos que vienen con datos

### 20.7 Documentación API
- Swagger UI disponible en `/api/swagger`
- OpenAPI docs en `/api/docs`

---

## 21. Observaciones y Mejoras

### 21.1 Aspectos Positivos
- Arquitectura hexagonal bien implementada
- Separación clara de capas
- Uso de DTOs para APIs
- Excepciones personalizadas
- Documentación Swagger
- Soft delete implementado
- Mapeo completo de entidades funcionando

### 21.2 Áreas de Mejora
- `deleteClient()` no implementado en ClientAdapter
- `ClientController.deleteClient()` retorna null
- Diferencia de implementación entre Client y Product:
  - Client usa ClientMapper manual
  - Product usa MapStruct para entity mapper
- No hay transacciones explícitas (@Transactional)
- No hay validación de entrada con Bean Validation
- Falta testing

---

## 22. Correcciones Realizadas

### 22.1 Problemas Corregidos

1. **ClientMapper (infraestructure/adapter/mapper/ClientMapper.java)**
   - Agregado mapeo de `addressId` en `mapAddressesToDomain()`
   - Agregado mapeo de `country` en `mapAddressesToDomain()`

2. **ClientAdapter**
   - Cambiado `mapToDomainBasic` por `mapToDomainWithAddresses` en `saveClient()`
   - Cambiado `mapToDomainBasic` por `mapToDomainWithAddresses` en `getAllClients()`

3. **ClientMapper REST (infraestructure/rest/mapper/ClientMapper.java)**
   - Agregado mapeo de `clientId` en `mapToClientResponse()`
   - Agregado mapeo de `address` en `mapToClientResponse()`

4. **ProductEntityMapper**
   - Agregado `@Mapping(target = "orders", ignore = true)`

5. **ProductMapper**
   - Agregado mapeo de `productId` y `isActive` en `mapToDTO()`

6. **AddressMapper**
   - Corregido mapeo de `zipCode` <- `postalCode` en `mapToResponse()`

### 22.2 Estado Actual

- **Compilación**: ✅ EXITOSA
- **API Products**: ✅ FUNCIONANDO
- **API Clients**: ✅ FUNCIONANDO (con address)
- **Swagger UI**: ✅ Disponible en `/api/swagger`

---

## 23. Resumen de Componentes

| Capa | Componentes |
|------|-------------|
| **Dominio** | 6 modelos + 4 interfaces (ports) + 1 servicio |
| **Aplicación** | 2 Use Cases |
| **Infraestructura** | 2 Controllers + 2 Adapters + 2 Repositorios + 5 Entities |
| **Errores** | 2 Excepciones + 2 Errores + 1 Handler |
| **DTOs** | 6 Request/Response + 4 Mappers |

---

## 24. Endpoints de la API

### Clientes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/clients | Crear cliente |
| GET | /api/clients | Listar todos los clientes |
| GET | /api/clients/{id} | Obtener cliente por ID |
| PUT | /api/clients/{id} | Actualizar cliente |
| DELETE | /api/clients/{id} | Eliminar cliente |

### Productos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/products | Crear producto |
| GET | /api/products | Listar todos los productos |
| GET | /api/products/{id} | Obtener producto por ID |
| PUT | /api/products/{id} | Actualizar producto |
| DELETE | /api/products/{id} | Eliminar producto |

---

## 25. Tecnologías Utilizadas

- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring Web** - API REST
- **MySQL 8.3.0** - Base de datos
- **MapStruct 1.6.3** - Mapeo de objetos
- **Lombok** - Reducción de boilerplate
- **SpringDoc OpenAPI 2.8.10** - Documentación automática
- **Hibernate** - ORM
- **Java 21** - Versión de Java
