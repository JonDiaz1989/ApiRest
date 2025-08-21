# 🚀 API REST - Gestión de Usuarios

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue)](https://maven.apache.org/)
[![H2](https://img.shields.io/badge/Database-H2-lightblue)](http://www.h2database.com/)
[![JWT](https://img.shields.io/badge/Auth-JWT-red)](https://jwt.io/)

API RESTful para creación y gestión de usuarios con autenticación JWT, construida con Spring Boot y base de datos H2 en memoria.

## 📋 Tabla de Contenidos
- [Características](#-características)
- [Arquitectura](#️-arquitectura)
- [Instalación](#-instalación)
- [Endpoints](#-endpoints)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Testing](#-testing)
- [Documentación](#-documentación)
- [Troubleshooting](#️-troubleshooting)

## ✨ Características

- ✅ **CRUD Completo**: Crear, leer, actualizar y eliminar usuarios
- ✅ **Autenticación JWT**: Tokens seguros para proteger endpoints
- ✅ **Validaciones**: Email único y formato de contraseña configurable
- ✅ **Base de Datos H2**: Almacenamiento en memoria para desarrollo
- ✅ **Documentación Swagger**: API docs interactiva
- ✅ **Manejo de Excepciones**: Respuestas de error estandarizadas
- ✅ **Tests Unitarios**: Cobertura de casos principales
- ✅ **Arquitectura por Capas**: Separación clara de responsabilidades

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura por capas**:

```
┌─────────────────┐
│   Controller    │ ← Manejo HTTP requests/responses
├─────────────────┤
│    Service      │ ← Lógica de negocio
├─────────────────┤
│   Repository    │ ← Acceso a datos
├─────────────────┤
│   Database H2   │ ← Almacenamiento
└─────────────────┘
```

**Componentes principales:**
- **UserController**: Endpoints REST
- **UserService**: Lógica de negocio y validaciones
- **UserRepository**: Operaciones de base de datos
- **JWT Security**: Autenticación y autorización
- **GlobalExceptionHandler**: Manejo centralizado de errores


Para mas detalles consulta los diagramas en [`/docs/diagrams.md`](docs/diagrams.md).

## 🚀 Instalación

### Requisitos del Sistema
- ☕ **Java 17** o superior
- 📦 **Maven 3.6.0** o superior
- 🔌 **Puerto 8080** disponible

### Pasos de Instalación

```bash
# 1. Clonar el repositorio
git clone https://github.com/JonDiaz1989/ApiRest.git
cd ApiRest

# 2. Compilar el proyecto
mvn clean install

# 3. Ejecutar la aplicación
mvn spring-boot:run
```

### ✅ Verificar Instalación

Una vez iniciada la aplicación, verifica que esté funcionando:

- **🌐 API Base**: http://localhost:8080
- **📚 Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **🗄️ H2 Console**: http://localhost:8080/h2-console
    - **URL JDBC**: `jdbc:h2:mem:testdb`
    - **Usuario**: `sa`
    - **Contraseña**: *(dejar vacío)*

## 📊 Endpoints

| Método | Endpoint | Auth Required | Descripción |
|--------|----------|---------------|-------------|
| 🟢 POST | `/users` | ❌ No | Crear nuevo usuario |
| 🔵 GET | `/users/getAllUsers` | ✅ Sí | Listar todos los usuarios |
| 🔵 GET | `/users/getUserById/{id}` | ✅ Sí | Obtener usuario específico |
| 🟡 PUT | `/users/updateUser` | ✅ Sí | Actualizar usuario existente |
| 🔴 DELETE | `/users/deleteUser/{id}` | ✅ Sí | Eliminar usuario |

> **⚠️ Nota**: Los endpoints protegidos requieren header `Authorization: Bearer {token}`

## 💻 Ejemplos de Uso

### 1. Crear Usuario (Sin autenticación)

```bash
curl --location --request POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.cl",
    "password": "Hunter123",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "countrycode": "57"
        }
    ]
}'
```

**Respuesta Exitosa (201 Created):**
```json
{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "created": "2024-01-15T10:30:00.000Z",
    "modified": "2024-01-15T10:30:00.000Z",
    "lastLogin": "2024-01-15T10:30:00.000Z",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "active": true
}
```

**Respuesta Error (409 Conflict):**
```json
{
    "mensaje": "El correo ya está registrado"
}
```

### 2. Obtener Todos los Usuarios

```bash
curl --location --request GET 'http://localhost:8080/users/getAllUsers' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
```

### 3. Obtener Usuario por ID

```bash
curl --location --request GET 'http://localhost:8080/users/getUserById/550e8400-e29b-41d4-a716-446655440000' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
```

### 4. Actualizar Usuario

```bash
curl --location --request PUT 'http://localhost:8080/users/updateUser' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Juan Carlos Rodriguez",
    "email": "juan@rodriguez.cl",
    "password": "NewPassword123",
    "phones": [
        {
            "number": "987654321",
            "citycode": "2",
            "countrycode": "56"
        }
    ]
}'
```

> **📝 Nota**: El email se usa para identificar al usuario a actualizar.

### 5. Eliminar Usuario

```bash
curl --location --request DELETE 'http://localhost:8080/users/deleteUser/550e8400-e29b-41d4-a716-446655440000' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'
```

## 🧪 Testing

### Ejecutar Todas las Pruebas
```bash
mvn test
```

### Ver Reporte de Cobertura
```bash
mvn jacoco:report
open target/site/jacoco/index.html
```

### Tipos de Pruebas
- **Unitarias**: Testean componentes individuales
- **Integración**: Testean flujo completo de endpoints
- **Validaciones**: Testean reglas de negocio

## 📚 Documentación

### Swagger UI
Una vez iniciada la aplicación, accede a la documentación interactiva:
```
http://localhost:8080/swagger-ui/index.html
```

### H2 Console
Para inspeccionar la base de datos en desarrollo:
```
http://localhost:8080/h2-console
```

**Configuración de conexión:**
- **Driver Class**: `org.h2.Driver`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User Name**: `sa`
- **Password**: *(vacío)*

## 🛠️ Troubleshooting

### ❌ Puerto 8080 ya está en uso

```bash
# Ver qué proceso usa el puerto
lsof -i :8080

# O cambiar puerto en application.properties
server.port=8081
```

### ❌ H2 Console no carga

- ✅ Verificar URL: `http://localhost:8080/h2-console`
- ✅ Usar JDBC URL exacta: `jdbc:h2:mem:testdb`
- ✅ Usuario: `sa`, Password: vacío

### ❌ Error "JWT token inválido"

- 🔄 Los tokens expiran en 24 horas
- 🔑 Crear nuevo usuario para obtener token fresco
- 📋 Verificar formato header: `Authorization: Bearer {token}`

### ❌ Error "El correo ya está registrado"

- 📧 Cada usuario debe tener email único
- 🗑️ Eliminar usuario existente o usar email diferente
- 🔄 Reiniciar aplicación para limpiar BD en memoria

### ❌ Error de formato de email o contraseña
####Email debe seguir patrón: aaaaaaa@dominio.cl
```json

{
    "mensaje": "Formato de email inválido"
}
```
#### Contraseña debe tener: mín 8 chars, 1 mayúscula, 1 minúscula, 1 número
```json
{
    "mensaje": "Formato de contraseña inválido"
}
```

### ❌ Maven build falla

```bash
# Limpiar y reinstalar dependencias
mvn clean install -U

# Skip tests temporalmente si es necesario
mvn clean install -DskipTests
```

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/apirest/
│   │   ├── 🎮 controller/          # REST Controllers
│   │   │   └── UserController.java
│   │   ├── 🔧 service/             # Business Logic  
│   │   │   ├── IUserService.java
│   │   │   └── UserService.java
│   │   ├── 🗄️ repository/          # Data Access
│   │   │   └── UserRepository.java
│   │   ├── 📊 model/               # Entities
│   │   │   ├── User.java
│   │   │   └── Phone.java
│   │   ├── 📦 dto/                 # Data Transfer Objects
│   │   │   ├── UserRequest.java
│   │   │   ├── UserResponse.java
│   │   │   └── ErrorResponse.java
│   │   ├── ⚙️ config/              # Configuration
│   │   │   ├── SecurityConfig.java
│   │   │   └── SwaggerConfig.java
│   │   ├── 🚨 exception/           # Exception Handling
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── CustomExceptions.java
│   │   ├── ✅ validator/           # Business Validation
│   │   │   └── UserValidator.java
│   │   └── 🔑 util/                # Utilities
│   │       └── JWTUtil.java
│   └── resources/
│       ├── application.properties  # App Configuration
│       ├── data.sql               # Initial Data (if any)
│       └── schema.sql             # DB Schema (if any)
└── test/
    ├── 🧪 unit/                    # Unit Tests
    └── 🔗 integration/             # Integration Tests

docs/                              # 📚 Technical Documentation
├── README.md                      # Architecture Details  
└── images/                        # Diagrams
    ├── architecture.png
    ├── user-flow.png
    └── database-model.png
```



## 🔒 Seguridad

- **🔐 JWT Tokens**: Autenticación stateless
- **🛡️ Password Validation**: Reglas configurables de contraseña
- **📧 Email Validation**: Formato y unicidad garantizada
- **🚫 SQL Injection**: Protección via JPA/Hibernate
- **⏰ Token Expiration**: Tokens con tiempo de vida limitado

## 🚀 Características Técnicas

- **☕ Java 17**: Versión LTS con últimas características
- **🍃 Spring Boot**: Framework enterprise-grade
- **🔗 Spring Data JPA**: ORM y manejo de datos
- **🗄️ H2 Database**: Base de datos en memoria para desarrollo
- **🔒 Spring Security**: Autenticación y autorización
- **📚 Swagger/OpenAPI**: Documentación automatizada
- **🧪 JUnit 5**: Framework de testing moderno
- **📦 Maven**: Gestión de dependencias y build

## 🤝 Contribuir

1. **Fork** el repositorio
2. Crear **branch** de feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** los cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** al branch (`git push origin feature/AmazingFeature`)
5. Abrir **Pull Request**

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

- **Jonathan Díaz** - [JonDiaz1989](https://github.com/JonDiaz1989)

## 🙏 Reconocimientos

- Spring Boot Team por el excelente framework
- H2 Database por la base de datos liviana
- JWT.io por la especificación de tokens
- Swagger por las herramientas de documentación

---

