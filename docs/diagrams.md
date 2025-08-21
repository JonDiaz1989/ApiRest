# Diagrams

## Architecture

```mermaid
flowchart LR
Client[Client / Swagger UI / Postman] --> API[/Spring Boot API/]
subgraph App
API --> C(UsersControllerV1)
C --> S(UserService)
S --> R[(UserRepository)]
R --> H2[(H2 In-Memory DB)]
end
```

## Components
```mermaid
flowchart TB
  subgraph users
    CTRL[Controller];
    SVC[Service];
    DTO["DTOs: UserDto\nUpdateUserDto\nPhoneDto\nUserResponse"];
    REP[Repository];
    ENT["Entities: User\nPhone"];
  end
  DTO --> CTRL
  CTRL --> SVC
  SVC --> REP
  REP --> ENT
  SVC --> ENT
```

## Sequence: Create User

```mermaid
sequenceDiagram
participant U as Client
participant C as UsersControllerV1
participant S as UserService
participant R as UserRepository
U->>C: POST /api/v1/users {nombre, correo, contraseña, telefonos[]}
C->>S: create(dto)
S->>S: validar email + password + duplicado
S->>R: save(User)
R-->>S: User
S-->>C: User
C-->>U: 201 Created + Location + ApiResponse<UserResponse>
```
## Sequence: Update User (PUT)

```mermaid
sequenceDiagram
  participant U as Client
  participant C as UsersControllerV1
  participant S as UserService
  participant PV as PasswordValidator
  participant PR as PhoneRepository
  participant UR as UserRepository

  U->>C: PUT /api/v1/users/{id} {nombre, contraseña, telefonos[]}
  C->>UR: findById(id)
  UR-->>C: User | not found
  alt found
    C->>S: updateUser(dto+id)
    opt contraseña presente
      S->>PV: validate(password)
      PV-->>S: ok | throws
    end
    S->>UR: save(user cambios básicos)
    S->>PR: deleteByUser(user)
    loop phones
      S->>PR: save(phone)
    end
    S->>UR: save(user con phones)
    S-->>C: User
    C-->>U: 200 OK (ApiResponse<UserResponse>)
  else not found
    C-->>U: 404 {"mensaje":"Recurso no encontrado"}
  end
```

## Sequence: Delete User

```mermaid
sequenceDiagram
  participant U as Client
  participant C as UsersControllerV1
  participant UR as UserRepository

  U->>C: DELETE /api/v1/users/{id}
  C->>UR: findById(id)
  alt found
    C->>UR: deleteById(id)
    C-->>U: 204 No Content
  else not found
    C-->>U: 404 {"mensaje":"Recurso no encontrado"}
  end
```

## Validación y Persistencia (Create)

```mermaid
flowchart TD
  A[POST /api/v1/users] --> B[Binding DTO]
  B --> C{Email válido?}
  C -- No --> X[400 El correo no es válido]
  C -- Sí --> D{Password válida?}
  D -- No --> Y[400 La contraseña no cumple el formato]
  D -- Sí --> E{Email duplicado?}
  E -- Sí --> Z[400 El correo ya está registrado]
  E -- No --> F[Mapear DTO -> User]
  F --> G[Hash password]
  G --> H[Set timestamps + token + active]
  H --> I[Mapear phones]
  I --> J[UserRepository.save]
  J --> K[201 Created + Location + UserResponse]
```

## State: Ciclo de vida de User

```mermaid
stateDiagram-v2
  [*] --> Created
  Created --> Active: persisted
  Active --> Active: update profile
  Active --> Deleted: delete
  Deleted --> [*]
```

## Data Model

```mermaid
classDiagram
class User {
UUID id
String name
String email
String password
LocalDateTime createdAt
LocalDateTime updatedAt
LocalDateTime lastLoginAt
String token
boolean active
}

class Phone {
UUID id
String number
String cityCode
String countryCode
}

User "1" --> "*" Phone
```

## ER: Modelo Relacional

```mermaid
erDiagram
  USER {
    UUID id PK
    string name
    string email
    string password
    datetime created_at
    datetime updated_at
    datetime last_login_at
    string token
    boolean active
  }

  PHONE {
    UUID id PK
    string number
    string city_code
    string country_code
    UUID user_id FK
  }

  USER ||--o{ PHONE : has
```