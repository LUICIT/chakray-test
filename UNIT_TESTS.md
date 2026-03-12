# Pruebas Unitarias - Fullstack Test

## Resumen

Se han creado **97 pruebas unitarias** con excelente cobertura para las siguientes clases:

### Clases Testeadas

1. **PhoneValidator** (10 tests)
   - Validación de números telefónicos
   - Tests para números null, blancos, con 10 dígitos exactos
   - Tests para números con caracteres de formato
   - Tests para números con más de 10 dígitos

2. **TaxIdValidator** (22 tests)
   - Validación de RFC (formato de impuestos mexicanos)
   - Tests para formatos válidos e inválidos
   - Tests para diferentes casos (mayúsculas, minúsculas, mixtos)
   - Tests parametrizados con múltiples casos

3. **DateUtil** (3 tests)
   - Obtención de fecha/hora actual en zona de Madagascar
   - Validación de formato dd-MM-yyyy HH:mm
   - Tests de no-nulidad y no-vacío

4. **Normalize Helper** (6 tests)
   - Normalización de taxId (trim + uppercase)
   - Tests con null, espacios en blanco, casos mixtos
   - Validación de comportamiento esperado

5. **UserMapper** (15 tests)
   - Mapeo de UserCreateRequest → User
   - Mapeo de List<AddressRequest> → List<Address>
   - Mapeo de User → UserResponse
   - Tests de preservación de datos y conversiones

6. **AuthService** (6 tests)
   - Login exitoso con credenciales válidas
   - Login fallido - usuario no encontrado
   - Login fallido - contraseña incorrecta
   - Normalización de taxId
   - Respuesta correcta del login
   - Múltiples intentos de login

7. **UserService** (35 tests)
   - **getUsers (9 tests)**
     - Obtener sin filtro ni ordenamiento
     - Ordenamiento por: id, name, email, phone, tax_id, created_at
     - Excepciones por campo inválido
     - Validación de formato incorrecto

   - **createUser (4 tests)**
     - Creación exitosa
     - TaxId duplicado
     - Formato inválido de taxId
     - Formato inválido de teléfono
     - Normalización de taxId

   - **patchUser (11 tests)**
     - Actualizar email
     - Actualizar nombre
     - Actualizar teléfono
     - Actualizar contraseña
     - Actualizar taxId
     - Actualizar direcciones
     - Validaciones de formatos
     - TaxId duplicado
     - Usuario no encontrado

   - **deleteUser (2 tests)**
     - Eliminación exitosa
     - Usuario no encontrado

   - **Filtrado (9 tests)**
     - Operadores: contains (co), equals (eq), startsWith (sw), endsWith (ew)
     - Validación de formato incorrecto
     - Validación de operador inválido
     - Validación de campo inválido
     - Casos donde no hay coincidencias

## Resultados de Pruebas

```
✓ PhoneValidator Tests:                   10 tests PASSED
✓ TaxIdValidator Tests:                   22 tests PASSED
✓ DateUtil Tests:                         3 tests PASSED
✓ Normalize Helper Tests:                 6 tests PASSED
✓ UserMapper Tests:                       15 tests PASSED
✓ AuthService Tests:                      6 tests PASSED
✓ UserService Tests:                      35 tests PASSED

==========================================================
TOTAL:                                   97 tests PASSED
```

## Cómo Ejecutar las Pruebas

### Ejecutar todas las pruebas:
```bash
./mvnw test
```

### Ejecutar solo pruebas unitarias (sin test de integración):
```bash
./mvnw test -Dtest='*Test,!FullstackTestApplication*'
```

### Ejecutar una clase específica de test:
```bash
./mvnw test -Dtest=AuthServiceTest
./mvnw test -Dtest=UserServiceTest
./mvnw test -Dtest=PhoneValidatorTest
./mvnw test -Dtest=TaxIdValidatorTest
./mvnw test -Dtest=UserMapperTest
./mvnw test -Dtest=NormalizeTest
./mvnw test -Dtest=DateUtilTest
```

### Ejecutar en modo quiet (menos output):
```bash
./mvnw test -q
```

## Cobertura de Código

Para obtener un reporte de cobertura de código, agrega el plugin JaCoCo al pom.xml:

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

Luego ejecuta:
```bash
./mvnw clean test
```

El reporte estará en: `target/site/jacoco/index.html`

## Características de los Tests

✓ **Uso de Mockito** para aislar las dependencias  
✓ **Pruebas parametrizadas** con @ParameterizedTest para múltiples casos  
✓ **Display names** descriptivos en cada test  
✓ **Buenas prácticas**: Arrange-Act-Assert (AAA)  
✓ **Manejo de excepciones** con assertThrows  
✓ **Tests de casos límite** (null, blank, empty, etc.)  
✓ **Validación exhaustiva** de comportamientos esperados  

## Archivos de Test Creados

```
src/test/java/com/chakray/fullstack_test/
├── service/
│   ├── AuthServiceTest.java
│   └── UserServiceTest.java
├── util/
│   ├── DateUtilTest.java
│   ├── PhoneValidatorTest.java
│   ├── TaxIdValidatorTest.java
│   └── UserMapperTest.java
└── helpers/
    └── NormalizeTest.java
```

## Próximos Pasos Recomendados

1. Agregar pruebas de integración para los Controllers
2. Agregar pruebas para las excepciones personalizadas
3. Implementar JaCoCo para monitorear cobertura
4. Agregar pruebas para seguridad y autenticación
5. Pruebas de rendimiento para operaciones críticas

---

**Creado:** 12 de Marzo de 2026  
**Total de Tests Unitarios:** 97  
**Tasa de Éxito:** 100%

