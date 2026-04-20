# 🧩 Sistema de Gestión de Empleados (Spring Boot + Thymeleaf + Tailwind)

## 📌 Descripción del Proyecto

Este proyecto consiste en el desarrollo de un sistema web de gestión de empleados orientado a entornos empresariales internos. La aplicación permitirá administrar empleados mediante operaciones CRUD, con autenticación de usuarios y una interfaz tipo dashboard.

El objetivo es construir una aplicación sólida basada en arquitectura MVC, utilizando tecnologías modernas del ecosistema Java, con una interfaz limpia y profesional.

---

## 🎯 Objetivos

* Implementar una aplicación web con arquitectura MVC
* Gestionar empleados (alta, baja lógica, edición y listado)
* Aplicar buenas prácticas de desarrollo backend
* Diseñar una interfaz profesional tipo panel administrativo
* Integrar base de datos relacional (PostgreSQL)
* Preparar el proyecto como pieza de portfolio profesional

---

## 🏗️ Arquitectura

### 🔹 Backend

* Java + Spring Boot
* Spring MVC
* Spring Data JPA
* (Opcional) Spring Security

### 🔹 Frontend

* Thymeleaf (renderizado del lado del servidor)
* Tailwind CSS (estilos)

### 🔹 Base de Datos

* PostgreSQL

---

## 📦 Estructura del Proyecto

```
src/main/java/com/empresa/sistema
│
├── controller
├── service
├── repository
├── entity
├── dto (opcional)
└── config (seguridad, etc)
```

```
src/main/resources
│
├── templates (Thymeleaf)
├── static
│   ├── css
│   └── js
└── application.yml
```

---

## 🧑‍💼 Entidad Principal: Empleado

### 📄 Campos

* id (Long)
* legajo (String, único)
* nombre (String)
* apellido (String)
* email (String)
* telefono (String)
* cargo (String)
* estado (Boolean → activo/inactivo)
* fechaIngreso (LocalDate)
* fechaCreacion (LocalDateTime)
* creadoPor (String)

---

## 🔐 Sistema de Usuarios (opcional pero recomendado)

* Login de usuarios
* Roles (ADMIN / USER)
* Visualización del usuario logueado en la interfaz

---

## 🧩 Funcionalidades

### ✅ Gestión de Empleados

* Crear empleado
* Editar empleado
* Listar empleados
* Baja lógica (cambiar estado a inactivo)

---

### 🔍 Búsqueda

* Por nombre
* Por legajo

---

### 📄 Listado

* Tabla con datos principales
* Botones de acción:

    * Editar
    * Desactivar

---

### 🔄 Estado del Empleado

* Activo / Inactivo
* Evitar borrado físico

---

### 📊 Dashboard (básico)

* Cantidad de empleados activos
* Cantidad de empleados inactivos

---

## 🖥️ Diseño de Interfaz

### 🔷 Layout General

#### HEADER

* Nombre del sistema
* Usuario logueado
* Rol
* Hora actual

#### SIDEBAR

* Dashboard
* Empleados
* Alta empleado
* Configuración

#### MAIN CONTENT

* Contenido dinámico según sección

---

## 🎨 Estilo Visual

* Uso de Tailwind CSS
* Diseño tipo panel administrativo
* Colores neutros (gris, blanco, azul)
* Tarjetas con sombras suaves
* Botones consistentes

---

## 🔁 Flujo Básico

1. Usuario accede al sistema
2. Visualiza listado de empleados
3. Puede:

    * Crear nuevo empleado
    * Editar existente
    * Desactivar empleado
4. Cambios impactan en la base de datos
5. Vista se actualiza automáticamente

---

## ⚙️ Configuración Base (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/empleados_db
    username: postgres
    password: password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  thymeleaf:
    cache: false

server:
  port: 8080
```

---

## 🚀 Posibles Mejoras Futuras

* Implementar Spring Security completo
* Auditoría avanzada (logs de cambios)
* Exportación a Excel/PDF
* API REST paralela
* Integración con frontend (React)

---

## 💼 Valor para Portfolio

Este proyecto demuestra:

* Manejo de arquitectura MVC
* Uso de Spring Boot en aplicaciones reales
* Integración con base de datos relacional
* Desarrollo de interfaces administrativas
* Aplicación de buenas prácticas

---

## 🧠 Conclusión

El sistema representa un caso de uso real dentro de entornos empresariales, combinando simplicidad y funcionalidad. Está diseñado para ser escalable y evolucionar hacia arquitecturas más complejas en el futuro.

---
