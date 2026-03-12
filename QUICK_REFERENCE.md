# 📋 QUICK REFERENCE - Comandos y Recursos

## 🚀 Ejecutar Tests

```bash
# Todos los tests
./mvnw test

# Solo unitarios (excluyendo integración)
./mvnw test -Dtest='*Test,!FullstackTestApplication*'

# Clase específica
./mvnw test -Dtest=UserServiceTest
./mvnw test -Dtest=AesEncryptionServiceTest
./mvnw test -Dtest=GlobalExceptionHandlerTest

# Método específico
./mvnw test -Dtest=UserServiceTest#testCreateUserSuccess

# Con debug
./mvnw test -X

# Compilar y ejecutar
./mvnw clean verify
```

## 📊 Coverage y Reportes

```bash
# Con JaCoCo (una vez configurado)
./mvnw clean test jacoco:report
open target/site/jacoco/index.html

# Con SonarQube
./mvnw clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000

# Ver resultados en consola
./mvnw test | grep -E "Tests run|Failures|Errors"
```

## 📁 Archivos de Referencia

| Archivo               | Contenido                       |
|-----------------------|---------------------------------|
| `TESTS_COMPLETED.md`  | Resumen final y estadísticas    |
| `UNIT_TESTS.md`       | Documentación completa de tests |
| `COVERAGE_GUIDE.md`   | Guía para mejorar coverage      |
| `TESTING_EXAMPLES.md` | Ejemplos de extensión           |
| `TEST_SUMMARY.md`     | Resumen ejecutivo               |

## 🧪 Tests Disponibles

```
PhoneValidatorTest ........................... 10 tests
├─ Validación null/blank
├─ Exactamente 10 dígitos
├─ Con caracteres de formato
├─ Con más de 10 dígitos
└─ Casos especiales

TaxIdValidatorTest ........................... 22 tests
├─ Formatos válidos
├─ Normalizacion (upper/lower/mixed)
├─ Validación de estructura
└─ Casos inválidos

DateUtilTest ................................ 3 tests
├─ Retorna fecha actual
├─ Formato correcto
└─ No null/empty

NormalizeTest ............................... 6 tests
├─ Trim + uppercase
├─ Null handling
└─ Casos especiales

UserMapperTest ............................. 15 tests
├─ UserCreateRequest → User
├─ AddressRequest[] → Address[]
├─ User → UserResponse
└─ Preservación de datos

AuthServiceTest ............................. 6 tests
├─ Login exitoso
├─ Usuario no encontrado
├─ Contraseña incorrecta
└─ Normalización

UserServiceTest ........................... 35 tests
├─ getUsers (9 tests)
├─ createUser (5 tests)
├─ patchUser (11 tests)
├─ deleteUser (2 tests)
└─ Filter operations (8 tests)

AesEncryptionServiceTest .................. 13 tests
├─ Encriptación exitosa
├─ Validaciones de key/algorithm
├─ Caracteres especiales y unicode
└─ Casos límite

GlobalExceptionHandlerTest ............... 17 tests
├─ ResourceNotFoundException
├─ BadRequestException
├─ UnauthorizedException
├─ MethodArgumentNotValidException
└─ Excepciones genéricas

TOTAL: 127 tests
```

## 💻 Setup de Mocks

```java
// Patrón básico con Mockito
@Mock
private UserRepository userRepository;

@InjectMocks
private UserService userService;

// Configurar
when(userRepository.findById(id)).thenReturn(Optional.of(user));

// Verificar
verify(userRepository).findById(id);
verify(userRepository, times(2)).save(any());
verify(userRepository, never()).delete(any());
```

## ✅ Patrón AAA

```java
@Test
void testMethod() {
    // ARRANGE - Preparar datos y mocks
    String input = "test";
    when(mock.method()).thenReturn("result");
    
    // ACT - Ejecutar el método
    String actual = service.methodBeingTested(input);
    
    // ASSERT - Validar resultado
    assertEquals("expected", actual);
    
    // VERIFY - Verificar interacciones (si es necesario)
    verify(mock).method();
}
```

## 🔍 Assertions Comunes

```java
// Básicas
assertEquals(expected, actual);
assertNotNull(object);
assertTrue(condition);
assertFalse(condition);
assertNull(object);

// Colecciones
assertEquals(3, list.size());
assertTrue(list.contains(item));
assertTrue(list.isEmpty());

// Excepciones
assertThrows(Exception.class, () -> method());
Exception ex = assertThrows(Exception.class, () -> method());
assertEquals("message", ex.getMessage());

// Valores
assertNotEquals(a, b);
assertTrue(value > 0);
assertTrue(string.matches(regex));
```

## 🏷️ Anotaciones Importantes

```java
// JUnit 5
@Test
@DisplayName("Description")
@ParameterizedTest
@ValueSource(strings = {"a", "b", "c"})
@BeforeEach
@AfterEach

// Mockito
@Mock
@InjectMocks
@ExtendWith(MockitoExtension.class)

// Spring
@WebMvcTest
@DataJpaTest
@SpringBootTest
@MockBean
```

## 📈 Cómo Mejorar Coverage

1. **Identificar clases sin tests**
   ```bash
   ./mvnw clean test jacoco:report
   ```

2. **Agregar tests para casos límite**
   - null values
   - empty strings
   - empty collections
   - boundary values

3. **Probar excepciones**
   - Casos de error
   - Validaciones
   - Estados inválidos

4. **Probar interacciones**
   - Llamadas a métodos
   - Número de invocaciones
   - Argumentos pasados

## 🎯 Objetivos de Coverage

| Métrica | Meta | Herramienta |
|---------|------|------------|
| Line Coverage | 80%+ | JaCoCo |
| Branch Coverage | 75%+ | JaCoCo |
| Method Coverage | 85%+ | JaCoCo |
| Mutation Coverage | 70%+ | PIT |

## 🚨 Errores Comunes

```java
// ❌ MALO - Tests acoplados
@Test void test1() { data = createData(); }
@Test void test2() { useData(); } // Depende de test1

// ✅ CORRECTO - Tests independientes
@BeforeEach void setUp() { data = createData(); }

// ❌ MALO - Stubbing innecesario
when(mock.method1()).thenReturn("value");
// ... no se usa method1

// ✅ CORRECTO - Usar lenient() o remover
@Mock(lenient = true)
private MyClass mock;

// ❌ MALO - Assertions vagas
assertTrue(result);

// ✅ CORRECTO - Assertions claras
assertEquals("expected value", result);
assertTrue(result.contains("expected"));
```

## 📚 Recursos

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/)
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

## 🤝 Contribuir Tests

Para agregar nuevos tests:

1. Crear clase `ClassName Test.java` en la carpeta correcta
2. Usar `@DisplayName` para descripciones claras
3. Implementar patrón AAA
4. Usar nombres descriptivos de métodos
5. Cubrir casos límite y excepciones
6. Ejecutar `./mvnw test` para validar

## 📊 Estructura de Carpetas

```
src/test/java/com/chakray/fullstack_test/
├── exception/
│   └── GlobalExceptionHandlerTest.java
├── helpers/
│   └── NormalizeTest.java
├── security/service/
│   └── AesEncryptionServiceTest.java
├── service/
│   ├── AuthServiceTest.java
│   └── UserServiceTest.java
└── util/
    ├── DateUtilTest.java
    ├── PhoneValidatorTest.java
    ├── TaxIdValidatorTest.java
    └── UserMapperTest.java
```

---

**Última actualización:** 12 de Marzo de 2026  
**Estado:** Completo y funcional ✅

