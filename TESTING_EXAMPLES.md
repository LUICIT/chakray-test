# Guía de Ampliación de Tests - Ejemplos Prácticos

## Introducción

Este documento proporciona ejemplos prácticos para ampliar las pruebas unitarias a otros componentes del proyecto.

---

## 1. Tests para AesEncryptionService

### Descripción
El servicio de encriptación AES necesita pruebas para sus métodos de encriptación y validación.

### Código de Ejemplo

```java
package com.chakray.fullstack_test.security.service;

import com.chakray.fullstack_test.exception.BadRequestException;
import com.chakray.fullstack_test.exception.EncryptionException;
import com.chakray.fullstack_test.security.config.AesProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("AesEncryptionService Tests")
@ExtendWith(MockitoExtension.class)
class AesEncryptionServiceTest {

    @Mock
    private AesProperties aesProperties;

    @InjectMocks
    private AesEncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        when(aesProperties.getAlgorithm()).thenReturn("AES");
        when(aesProperties.getSecretKey()).thenReturn("12345678901234567890123456789012"); // 32 chars
    }

    @Test
    @DisplayName("Should encrypt password successfully")
    void testEncryptSuccess() {
        // Act
        String encrypted = encryptionService.encrypt("myPassword");

        // Assert
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
        assertNotEquals("myPassword", encrypted); // No debe ser igual al plaintext
    }

    @Test
    @DisplayName("Should throw exception with null algorithm")
    void testEncryptWithNullAlgorithm() {
        // Arrange
        when(aesProperties.getAlgorithm()).thenReturn(null);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> encryptionService.encrypt("test"));
    }

    @Test
    @DisplayName("Should throw exception with invalid secret key length")
    void testEncryptWithInvalidKeyLength() {
        // Arrange
        when(aesProperties.getSecretKey()).thenReturn("shortkey");

        // Act & Assert
        assertThrows(BadRequestException.class, () -> encryptionService.encrypt("test"));
    }

    @Test
    @DisplayName("Should produce consistent encryption results")
    void testEncryptConsistency() {
        // Act
        String encrypted1 = encryptionService.encrypt("password");
        String encrypted2 = encryptionService.encrypt("password");

        // Assert
        assertEquals(encrypted1, encrypted2);
    }
}
```

---

## 2. Tests para GlobalExceptionHandler

### Descripción
Validar que el manejador global de excepciones responde correctamente a diferentes tipos de excepciones.

### Código de Ejemplo

```java
package com.chakray.fullstack_test.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle BadRequestException")
    void testHandleBadRequestException() {
        // Arrange
        BadRequestException ex = new BadRequestException("Invalid input");

        // Act
        ResponseEntity<Object> response = handler.handleBadRequestException(ex, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof GlobalExceptionHandler.ApiError);
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException")
    void testHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        // Act
        ResponseEntity<Object> response = handler.handleResourceNotFoundException(ex, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle UnauthorizedException")
    void testHandleUnauthorizedException() {
        // Arrange
        UnauthorizedException ex = new UnauthorizedException("Invalid credentials");

        // Act
        ResponseEntity<Object> response = handler.handleUnauthorizedException(ex, null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should include error message in response")
    void testErrorMessageInResponse() {
        // Arrange
        String errorMsg = "Test error message";
        BadRequestException ex = new BadRequestException(errorMsg);

        // Act
        ResponseEntity<Object> response = handler.handleBadRequestException(ex, null);

        // Assert
        assertNotNull(response.getBody());
        // Validar que el mensaje está en la respuesta
    }
}
```

---

## 3. Tests para Controllers

### Descripción
Pruebas de endpoints REST usando MockMvc.

### Código de Ejemplo (cuando tengas Controllers)

```java
package com.chakray.fullstack_test.web.controller;

import com.chakray.fullstack_test.service.UserService;
import com.chakray.fullstack_test.web.dto.request.UserCreateRequest;
import com.chakray.fullstack_test.web.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("UserController Tests")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateRequest createRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        createRequest = new UserCreateRequest(
                "john@example.com",
                "John Doe",
                "5512345678",
                "password123",
                "AARR990101XXX",
                List.of()
        );

        userResponse = new UserResponse(
                UUID.randomUUID(),
                "john@example.com",
                "John Doe",
                "5512345678",
                "AARR990101XXX",
                "12-03-2026",
                List.of()
        );
    }

    @Test
    @DisplayName("Should get all users with status 200")
    void testGetAllUsers() throws Exception {
        // Arrange
        when(userService.getUsers(null, null)).thenReturn(List.of(userResponse));

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    @DisplayName("Should create user with status 201")
    void testCreateUserSuccess() throws Exception {
        // Arrange
        when(userService.createUser(any())).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Should return 400 for invalid request")
    void testCreateUserBadRequest() throws Exception {
        // Arrange
        UserCreateRequest invalidRequest = new UserCreateRequest(
                "invalid-email", // Invalid format
                "John",
                "123",           // Invalid phone
                "pass",
                "INVALID",       // Invalid taxId
                List.of()
        );

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should delete user and return 204")
    void testDeleteUser() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/users/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}
```

---

## 4. Tests para Excepciones

### Código de Ejemplo

```java
package com.chakray.fullstack_test.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Custom Exceptions Tests")
class CustomExceptionsTest {

    @Test
    @DisplayName("BadRequestException should have message")
    void testBadRequestException() {
        // Act & Assert
        BadRequestException ex = new BadRequestException("Invalid input");
        assertEquals("Invalid input", ex.getMessage());
    }

    @Test
    @DisplayName("ResourceNotFoundException should have message")
    void testResourceNotFoundException() {
        // Act & Assert
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        assertEquals("Not found", ex.getMessage());
    }

    @Test
    @DisplayName("UnauthorizedException should have message")
    void testUnauthorizedException() {
        // Act & Assert
        UnauthorizedException ex = new UnauthorizedException("Unauthorized");
        assertEquals("Unauthorized", ex.getMessage());
    }

    @Test
    @DisplayName("EncryptionException should have message")
    void testEncryptionException() {
        // Act & Assert
        EncryptionException ex = new EncryptionException("Encryption failed");
        assertEquals("Encryption failed", ex.getMessage());
    }
}
```

---

## 5. Patrón AAA Detallado

### Patrón Recomendado

```java
@Test
@DisplayName("Descripción clara del comportamiento esperado")
void testMethodName() {
    // ARRANGE (Preparar)
    // - Crear datos necesarios
    // - Configurar mocks
    String input = "test data";
    
    when(dependency.method()).thenReturn("expected result");

    // ACT (Actuar)
    // - Ejecutar el método siendo testeado
    String result = serviceUnderTest.methodBeingTested(input);

    // ASSERT (Afirmar)
    // - Validar resultados
    assertNotNull(result);
    assertEquals("expected result", result);
    
    // VERIFY (Verificar - si es necesario)
    // - Validar interacciones con mocks
    verify(dependency).method();
}
```

---

## 6. Mejores Prácticas

### ✅ DO's (Haz esto)

```java
// ✅ Nombres descriptivos
@Test
@DisplayName("Should throw exception when email is invalid")
void testEmailValidationFails() { }

// ✅ Setup en @BeforeEach
@BeforeEach
void setUp() {
    testData = createTestData();
}

// ✅ Usar constantes
private static final String VALID_EMAIL = "test@example.com";
private static final String INVALID_TAX_ID = "INVALID";

// ✅ Mocks específicos
@Mock private UserRepository userRepository;
when(userRepository.findById(id)).thenReturn(Optional.of(user));

// ✅ Assertions claras
assertEquals(expected, actual);
assertThrows(Exception.class, () -> methodCall());
```

### ❌ DON'Ts (No hagas esto)

```java
// ❌ Nombres genéricos
void test1() { }

// ❌ Tests dependientes
@Test
void test1() { /* Crea datos */ }
@Test
void test2() { /* Usa datos de test1 */ } // ¡MAL!

// ❌ Múltiples aserciones sin contexto
assertEquals(a, b);
assertEquals(c, d);
assertEquals(e, f);

// ❌ Lógica compleja en tests
if (condition) {
    // Test lógica
}

// ❌ Dormir en tests
Thread.sleep(1000);
```

---

## 7. Checklist para Nuevos Tests

- [ ] Nombre descriptivo del test
- [ ] Patrón AAA implementado
- [ ] Mocks configurados correctamente
- [ ] Casos de éxito cubiertos
- [ ] Casos de excepción cubiertos
- [ ] Casos límite cubiertos (null, empty, etc.)
- [ ] Assertions claras y significativas
- [ ] Independencia de otros tests
- [ ] No depende de orden de ejecución
- [ ] Runs rápido (< 1 segundo)

---

## 8. Ejecución de Tests Específicos

```bash
# Todos los tests
./mvnw test

# Clase específica
./mvnw test -Dtest=UserServiceTest

# Método específico
./mvnw test -Dtest=UserServiceTest#testCreateUserSuccess

# Con patrón
./mvnw test -Dtest=*Service*

# Con verbosidad
./mvnw test -X

# Failsafe (tests de integración)
./mvnw verify
```

---

## Referencias Útiles

- [JUnit 5 Annotations](https://junit.org/junit5/docs/current/user-guide/#writing-tests-annotations)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [AssertJ Assertions](https://assertj.github.io/assertj-core-features-highlight.html)

---

**Documento Creado:** 12 de Marzo de 2026

