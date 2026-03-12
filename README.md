# Chakray Consulting – Prueba Técnica Desarrollador Fullstack

## Descripción General

Este proyecto es una implementación de la **prueba técnica para la posición de Desarrollador Fullstack en Chakray Consulting**.

La aplicación expone una **API REST para gestión de usuarios y autenticación**, implementando filtrado, ordenamiento, validaciones y manejo seguro de contraseñas.

El objetivo de este proyecto es demostrar:

- Arquitectura limpia
- Buen diseño de APIs REST
- Validación de datos
- Manejo adecuado de errores
- Buenas prácticas de seguridad
- Código claro y mantenible

---

# Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 4.0.3**
- **Maven**
- **JUnit 5**
- **Mockito**
- **Swagger / OpenAPI**
- **Docker** (ejecución opcional en contenedor)

Entorno de desarrollo utilizado:

- IntelliJ IDEA
- Maven Wrapper

---

# Funcionalidades del Proyecto

La API soporta las siguientes funcionalidades:

## Autenticación

Autenticación de usuarios mediante **tax_id y password**.

Características:

- Cifrado de contraseñas utilizando **AES**
- Manejo adecuado de errores cuando las credenciales son inválidas

---

## Gestión de Usuarios

La API permite realizar las siguientes operaciones:

- Crear usuarios
- Consultar usuarios
- Actualizar usuarios
- Eliminar usuarios

---

## Filtrado y Ordenamiento

El endpoint `GET /users` permite realizar ordenamiento y filtrado de resultados.

### Ordenamiento

Ejemplo:

```
GET /users?sortedBy=name
```

Campos soportados:

- id
- email
- name
- phone
- tax_id
- created_at

---

### Filtrado

Ejemplo:

```
GET /users?filter=name+co+john
```

Operadores soportados:

| Operador | Descripción |
|--------|-------------|
| eq | igual a |
| co | contiene |
| sw | comienza con |
| ew | termina con |

---

# Reglas de Validación

## tax_id

El `tax_id` sigue un formato simplificado de **RFC para personas físicas en México**.

Formato:

```
AAAA000000XXX
```

Donde:

- `AAAA` → cuatro letras del nombre
- `000000` → seis dígitos que representan fecha de nacimiento
- `XXX` → tres caracteres alfanuméricos que representan la homoclave

Ejemplo:

```
AARR990101XXX
```

---

## phone

Los números telefónicos son validados mediante un validador personalizado implementado en el proyecto el cual puede contener el código del país.

```
5554567890
```
ó
```
+525554567890
```

---

# Manejo de Errores

El proyecto implementa un **manejador global de excepciones** utilizando `@RestControllerAdvice`.

Todos los errores siguen una estructura consistente de respuesta:

```json
{
  "timestamp": "2026-03-12T20:45:00",
  "status": 400,
  "message": "Validation error",
  "errors": {
    "email": "Email is required"
  }
}
```

---

# Ejecución del Proyecto

## Requisitos

Antes de ejecutar el proyecto asegúrate de tener instalado:

- **Java 21**
- **Maven 3.9 o superior**

---

## Ejecutar usando Maven

Desde la raíz del proyecto ejecutar:

```bash
./mvnw spring-boot:run
```

o bien:

```bash
mvn spring-boot:run
```

La aplicación iniciará en:

```
http://localhost:8080
```

---

# Documentación de la API

Swagger UI está disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

Desde esta interfaz el evaluador puede probar todos los endpoints de forma interactiva.

---

# Ejecución de Pruebas

Las pruebas unitarias están implementadas usando **JUnit 5 y Mockito**.

Para ejecutar las pruebas:

```bash
./mvnw test
```

---

# Soporte con Docker

La aplicación también puede ejecutarse usando Docker.

Construir la imagen:

```bash
docker build -t chakray-fullstack-test .
```

Ejecutar el contenedor:

```bash
docker run -p 8080:8080 chakray-fullstack-test
```

---

# Ejemplos de Endpoints

## Login

```
POST /login
```

Ejemplo de body:

```json
{
  "taxId": "AARR990101XXX",
  "password": "password123"
}
```

---

## Crear Usuario

```
POST /users
```

---

## Obtener Usuarios

```
GET /users
```

---

## Actualizar Usuario

```
PATCH /users/{id}
```

---

## Eliminar Usuario

```
DELETE /users/{id}
```

---

# Estructura del Proyecto

```
com.chakray.fullstack_test
├── config
├── domain
│   ├── model
│   └── repository
├── exception
├── helpers
├── security
│   ├── config
│   └── service
├── service
├── util
├── web
│   ├── controller
│   └── dto
└── FullstackTestApplication
```

---

# Notas para el Evaluador

- El proyecto fue implementado utilizando **Java 21 y Spring Boot 4.0.3**.
- La API puede explorarse completamente usando **Swagger UI**.
- Las pruebas unitarias pueden ejecutarse mediante Maven.
- La aplicación puede ejecutarse también mediante **Docker**.

Esta implementación busca demostrar:

- Código limpio y mantenible
- Buen diseño de APIs REST
- Validación adecuada de datos
- Manejo correcto de errores
- Una estructura clara del proyecto