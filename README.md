# API de Usuarios

Este proyecto es una API REST para la gestión de usuarios.

## Requisitos

Para ejecutar este proyecto necesitas:

- Java 17
- Maven 3.6.0

## Instrucciones de instalación

1. Clona el repositorio en tu máquina local.
2. Navega hasta el directorio del proyecto.
3. Ejecuta `mvn clean install` para construir el proyecto.
4. Ejecuta `mvn spring-boot:run` para iniciar la aplicación.

La aplicación se ejecutará en `http://localhost:8080`.

## Pruebas Unitarias

Ejecuta `mvn test` para ejecutar las pruebas unitarias del proyecto.

## Endpoints

### Curls de prueba para la API

Para probar los diferentes endpoints de la API, puedes usar el siguiente comando `curl` como se muestran a contiuación.
Tambien puedes copiarlos y pegarlos en postman para su ejecución.

#### getAllUsers

Para obtener todos los usuarios registrados en la base de datos:

```
curl --location --request GET 'http://localhost:8080/users/getAllUsers'
```

#### getUserById

Para obtener un usuario específico (reemplaza {id} con el ID del usuario):

```
curl --location --request GET 'http://localhost:8080/users/getUserById/{id}'
```

#### createUser

Para registrar usuarios en la base de datos:

```
curl --location --request POST 'http://localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Pedro Rodriguez",
    "email": "pedro@rodriguez.org",
    "password": "hunter2",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "contrycode": "57"
        }
    ]
}'
```

#### updateUser

Para actualizar un usuario (Actualiza todos los datos menos el email).
Debe reemplazarse el email {email} por el que se desea actualizar:

```
curl --location --request PUT 'http://localhost:8080/users/updateUser' \
--header 'Content-Type: application/json' \
--data-raw '{
        "name": "Pedro Rodriguez",
        "email": "{email}",
        "password": "hunter222",
        "phones": [
            {
                "number": "123456789",
                "citycode": "1",
                "contrycode": "56"
            }
        ]
    }'
```

#### deleteUser

Para actualizar un usuario (reemplaza {id} con el ID del usuario):

```
curl --location --request DELETE 'http://localhost:8080/users/deleteUser/{id}'
```

## Documentación Swagger UI

Una vez levantada la aplicaciíon puede verse la documentación de la API en Swagger UI en la siguiente URL:

```
http://localhost:8080/swagger-ui/index.html#/
```