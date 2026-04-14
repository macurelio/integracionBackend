
# Desafío Técnico - API de Registro de Usuarios

Este proyecto es una aplicación Spring Boot que expone una API RESTful para el registro de usuarios, utilizando JPA para la persistencia en una base de datos H2 en memoria.

## Tecnologías
- Java 8/17
- Spring Boot 2.7.18
- Spring Data JPA / Hibernate
- Base de datos H2 (en memoria)
- JWT (JJWT)
- Lombok
- SpringDoc OpenAPI (Swagger)

## Requisitos
- **Entrada/Salida JSON**: Todos los endpoints aceptan y devuelven datos en formato JSON.
- **Formato de errores**: Todos los errores retornan `{"mensaje": "descripción del error"}`.
- **Validaciones**:
  - Email: Expresión regular `aaaaaaa@dominio.cl`.
  - Contraseña: Expresión regular configurable (ver `application.yml`).
- **Token**: Se genera un JWT al registrarse y se persiste junto al usuario.
- **Persistencia**: Se utiliza UUID como clave primaria y se almacenan los timestamps (created, modified, last_login).

## ¿Cómo Probar la Aplicación?

### 1. Compilar y Ejecutar
Clona el proyecto y ejecútalo con Maven:
```bash
mvn spring-boot:run
```

### 2. Documentación de la API (Swagger)
Una vez que la aplicación esté en funcionamiento, puedes acceder a la interfaz Swagger UI para probar los endpoints:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. Consola H2
Puedes inspeccionar el contenido de la base de datos en:
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:userdb`
- Usuario: `sa`
- Contraseña: (dejar en blanco)

### 4. Endpoint de Registro
**POST** `http://localhost:8080/api/users/register`

**Ejemplo de cuerpo de solicitud:**
```json
{
  "name": "Juan Rodriguez",
  "email": "juan@dominio.cl",
  "password": "hunter2password",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
```

### 5. Script de Base de Datos
El script de creación de la base de datos se encuentra en `src/main/resources/db/script.sql`. Aunque la aplicación utiliza JPA para crear el esquema automáticamente en H2, este script se provee como referencia o para ejecución manual en otros entornos.

### 6. Diagrama de la Solución
Una representación visual de la arquitectura y el flujo está disponible en:
- [diagram.md](file:///c:/integracionBackend/docs/diagram.md) (Mermaid/Markdown)
- [diagram.drawio](file:///c:/integracionBackend/docs/diagram.drawio) (formato compatible con [diagrams.net](https://app.diagrams.net/))

Puedes visualizar el archivo `.drawio` importándolo en el editor web de [Draw.io/Diagrams.net](https://app.diagrams.net/).

## Arquitectura de la Solución
La aplicación sigue una arquitectura estándar en capas (N-tier):
- **Controller**: Gestiona las solicitudes y respuestas HTTP.
- **Service**: Contiene la lógica de negocio, validaciones y generación de JWT.
- **Repository**: Interfaz para el acceso a datos usando JPA.
- **Model**: Entidades JPA que representan el esquema de la base de datos.
- **Security**: Componentes utilitarios para JWT.
- **Exception**: Manejador global de excepciones para un formato de error unificado.
