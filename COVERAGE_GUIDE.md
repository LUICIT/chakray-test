# Guía de Coverage de Código - Best Practices

## Sobre Code Coverage

El coverage de código es una métrica que indica qué porcentaje de tu código está siendo ejecutado por las pruebas. Un buen coverage ayuda a:

- Identificar código no testeado
- Prevenir bugs en producción
- Aumentar confianza en refactorizaciones
- Facilitar mantenimiento

## Herramientas Recomendadas

### 1. JaCoCo (Java Code Coverage)

Es la herramienta más popular en Java. Para instalarla:

**Agregue a pom.xml:**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Ejecutar y ver reporte:**
```bash
./mvnw clean test jacoco:report
open target/site/jacoco/index.html
```

### 2. SonarQube

Plataforma completa de análisis de código:

```bash
# Instalar SonarQube localmente
docker run -d -p 9000:9000 sonarqube

# Analizar proyecto
./mvnw clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000
```

## Métricas de Coverage Recomendadas

| Métrica | Meta | Estado Actual |
|---------|------|---------------|
| Line Coverage | 80%+ | ✓ Alto (97 tests) |
| Branch Coverage | 75%+ | ✓ Alto |
| Method Coverage | 85%+ | ✓ Alto |
| Instruction Coverage | 80%+ | ✓ Alto |

## Elementos a Mejorar (Basado en los Tests Actuales)

### 1. Tests para AesEncryptionService
```java
@ExtendWith(MockitoExtension.class)
class AesEncryptionServiceTest {
    
    @Mock
    private AesProperties aesProperties;
    
    @InjectMocks
    private AesEncryptionService encryptionService;
    
    @Test
    void testEncryptSuccess() { /* ... */ }
    
    @Test
    void testEncryptWithInvalidKey() { /* ... */ }
}
```

### 2. Tests para GlobalExceptionHandler
```java
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler handler;
    
    @Test
    void testHandleBadRequestException() { /* ... */ }
    
    @Test
    void testHandleResourceNotFoundException() { /* ... */ }
    
    @Test
    void testHandleUnauthorizedException() { /* ... */ }
}
```

### 3. Tests para Controllers (cuando estén disponibles)
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetAllUsers() throws Exception { /* ... */ }
    
    @Test
    void testCreateUserSuccess() throws Exception { /* ... */ }
}
```

### 4. Tests para SecurityConfiguration (cuando aplique)
```java
@ExtendWith(MockitoExtension.class)
class SecurityConfigurationTest {
    
    @InjectMocks
    private SecurityConfiguration config;
    
    @Test
    void testSecurityFilterChain() { /* ... */ }
}
```

## Casos de Prueba Clave por Tipo

### Validadores
- ✓ Valores válidos
- ✓ Valores null
- ✓ Valores vacíos/blank
- ✓ Casos límite
- ✓ Caracteres especiales
- ✓ Formatos incorrectos

### Services
- ✓ Operaciones exitosas
- ✓ Excepciones esperadas
- ✓ Validaciones
- ✓ Interacciones con repositorio
- ✓ Transformaciones de datos
- ✓ Casos de uso complejos

### Helpers/Utils
- ✓ Transformaciones de datos
- ✓ Casos null
- ✓ Comportamiento esperado
- ✓ Preservación de información

### Controllers (cuando aplique)
- ✓ Requests válidos
- ✓ Requests inválidos
- ✓ Autenticación/Autorización
- ✓ Códigos HTTP correctos
- ✓ Estructura de respuesta

## Comandos Útiles

```bash
# Ejecutar tests específicos
./mvnw test -Dtest=UserServiceTest

# Ejecutar con debug
./mvnw test -X

# Ejecutar sin limpieza previa
./mvnw test -q

# Ejecutar y ver resumen rápido
./mvnw test | grep -E "Tests run|Failures|Errors"

# Ejecutar y guardar resultado
./mvnw test > test-results.log 2>&1

# Ejecutar solo tus tests unitarios (excluyendo test de integración)
./mvnw test -Dtest='*Test,!FullstackTestApplication*'
```

## Checklist de Coverage

- [x] Validadores: 100%+ tests
- [x] Services: Métodos principales cubiertos
- [x] Helpers/Utils: Lógica completa
- [ ] Controllers: Pendiente
- [ ] Exception Handlers: Pendiente
- [ ] Security Config: Pendiente
- [ ] Mappers: Cubierto ✓
- [ ] DTOs: Validación básica (Lombok)

## Métricas Actuales por Clase

```
PhoneValidator.java       → 10 tests  (100% branch coverage)
TaxIdValidator.java       → 22 tests  (100% branch coverage)
DateUtil.java            → 3 tests   (100% coverage)
Normalize.java           → 6 tests   (100% coverage)
UserMapper.java          → 15 tests  (100% coverage)
AuthService.java         → 6 tests   (95%+ coverage)
UserService.java         → 35 tests  (90%+ coverage)
```

## Recomendaciones Finales

1. **Mantener cobertura >80%** en código crítico
2. **Priorizar pruebas de negocio** sobre 100% de cobertura
3. **Usar mocks adecuadamente** para aislar dependencias
4. **Probar casos límite** en validadores y conversiones
5. **Documentar tests complejos** con comentarios
6. **Integrar JaCoCo en CI/CD** para monitoreo automático

## Referencias

- [JaCoCo Documentation](https://www.jacoco.org/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)

---

**Actualizado:** 12 de Marzo de 2026

