# Solution Diagram

This diagram represents the high-level architecture and flow of the User Registration API.

```mermaid
graph TD
    User((Client)) -->|POST /api/users/register| UI[UserController]
    UI -->|Validates Request| US[UserService]
    US -->|Regex Validation| US
    US -->|Check Database| UR[UserRepository]
    UR -->|Exist?| DB[(H2 DB)]
    US -->|Generate Token| JTP[JwtTokenProvider]
    US -->|Save User| UR
    UR -->|Persist| DB
    US -->|Return UserResponse| UI
    UI -->|201 Created| User

    subgraph Error Handling
        GEH[GlobalExceptionHandler] -->|Catch Exception| UI
        GEH -->|Return JSON Error| User
    end

    subgraph Entities
        UE[User Entity]
        PE[Phone Entity]
        UE "1" *-- "many" PE
    end
```

## Key Components
- **Persistence**: JPA/Hibernate manages the lifecycle of `User` and `Phone` entities.
- **Security**: JWT is used for token generation, ensuring stateless authentication potential.
- **Validation**: Centralized in the service layer for consistency across different entry points.
