# Technical Challenge - User Registration API

This project is a Spring Boot application that exposes a RESTful API for user registration with JPA persistence in an H2 in-memory database.

## Technologies
- Java 8/17
- Spring Boot 2.7.18
- Data JPA / Hibernate
- H2 Database (In-memory)
- JWT (JJWT)
- Lombok
- SpringDoc OpenAPI (Swagger)

## Requirements
- **JSON Input/Output**: All endpoints accept and return JSON.
- **Error Format**: All errors return `{"mensaje": "descripción del error"}`.
- **Validation**:
  - Email: Regular expression `aaaaaaa@dominio.cl`.
  - Password: Configurable regular expression (see `application.yml`).
- **Token**: JWT generated upon registration and persisted with the user.
- **Persistence**: UUID as primary key, timestamps (created, modified, last_login).

## How to Test

### 1. Build and Run
Clone the project and run it with Maven:
```bash
mvn spring-boot:run
```

### 2. API Documentation (Swagger)
Once the application is running, you can access the Swagger UI to test the endpoints:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. H2 Console
You can inspect the database content at:
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:userdb`
- User: `sa`
- Password: (blank)

### 4. Registration Endpoint
**POST** `http://localhost:8080/api/users/register`

**Request Body Example:**
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

### 5. Database Script
The database creation script can be found in `src/main/resources/db/script.sql`. Although the application uses JPA to automatically create the schema in H2, this script is provided for reference or manual execution in other environments.

### 6. Solution Diagram
A visual representation of the architecture and flow is available in:
- [diagram.md](file:///c:/integracionBackend/docs/diagram.md) (Mermaid/Markdown)
- [diagram.drawio](file:///c:/integracionBackend/docs/diagram.drawio) (Format compatible with [diagrams.net](https://app.diagrams.net/))

You can view the `.drawio` file by importing it into the [Draw.io/Diagrams.net](https://app.diagrams.net/) web editor.

## Solution Architecture
The application follows a standard N-tier architecture:
- **Controller**: Handles HTTP requests and responses.
- **Service**: Contains business logic, validation, and JWT generation.
- **Repository**: Interface for data access using JPA.
- **Model**: JPA entities representing the database schema.
- **Security**: JWT utility components.
- **Exception**: Global exception handler for unified error formats.
