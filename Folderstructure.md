# 📂 Project Folder Structure (Industry Standard with Our Conventions)

This document describes the **industry-standard folder/package structure** we are following in this Spring Boot project.  
We will always try to follow **MVC + layered architecture** standards.

---

## 📂 Final Structure We Will Maintain
```text
src/
 └── main/
     ├── java/
     │    └── com/
     │         └── samiransetua/
     │              └── forms/
     │                   ├── config/          # Configuration classes (CORS, Security, Swagger, etc.)
     │                   ├── controller/      # REST Controllers (API endpoints)
     │                   ├── dto/             # Data Transfer Objects (Request/Response payloads)
     │                   ├── entity/          # JPA Entities (database models) [our only exception]
     │                   ├── exception/       # Custom exceptions & handlers
     │                   ├── repository/      # Spring Data JPA repositories (DB access)
     │                   ├── service/         # Business logic (interfaces + impl)
     │                   ├── util/            # Utility/helper classes
     │                   └── FormsApplication.java  # Main Spring Boot application class
     │
     └── resources/
          ├── application.properties (or application.yml)  # Config
          ├── static/       # Static files (HTML, CSS, JS) if any
          ├── templates/    # Thymeleaf/Freemarker templates if used
          └── db/           # (Optional) migration scripts (if not using external `migrations/`)
              └── migration/ # Flyway or Liquibase migrations
 └── test/
      └── java/
           └── com/
                └── samiransetua/
                     └── forms/
                          ├── controller/   # Controller tests
                          ├── service/      # Service layer tests
                          └── repository/   # Repository tests

```

---

## 📖 Notes on Each Layer

- **`models/`** → Contains JPA Entities (tables).  
  ⚠️ Normally named `entity/`, but in our project we will use `models/` as an exception.

- **`repository/`** → Database access using Spring Data JPA.

- **`service/`** → Business logic goes here.
    - Define **interfaces** (e.g., `UserService`) and **implementation classes** (e.g., `UserServiceImpl`).
    - Keeps controllers thin by moving logic here.

- **`dto/`** → Data Transfer Objects for request & response payloads.
    - Prevents exposing JPA entities directly in APIs.
    - Example: `UserDto`, `LoginRequestDto`, `UserResponseDto`.

- **`controller/`** → REST Controllers (expose endpoints).
    - Should only delegate to services.
    - Never contain heavy business logic.

- **`config/`** → Configuration classes.
    - Security (JWT, OAuth), CORS setup, Swagger/OpenAPI, etc.

- **`exception/`** → Centralized exception handling.
    - Custom exceptions (`UserNotFoundException`) and global exception handler (`GlobalExceptionHandler`).

- **`util/`** → Utility/helper classes.
    - Example: JWT utility, password hashing, date formatter, etc.

- **`resources/db/migration/`** → Used if Flyway/Liquibase is added later for version-controlled DB schema changes.

---

## 🎯 Standards We Will Follow in This Project

1. ✅ **Controller → Service → Repository → Database** flow.
2. ✅ **DTO layer** for requests/responses (no exposing entities).
3. ✅ **Config layer** for all framework/security settings.
4. ✅ **Centralized exception handling**.
5. ✅ **Keep `models/` instead of `entity/`** (our only exception).

---

📌 With this, we have a **clear blueprint**. Every new feature (e.g., JWT auth, OAuth, secured resources) will follow this structure.

---

### 📝 Why Use Interfaces for Service Layer

In our project, every service layer uses an interface (e.g., `UserService`) and a corresponding implementation (e.g., `UserServiceImpl`). This is a best practice in industry-standard Spring Boot applications.

### 1️⃣ Separation of Contract vs Implementation

 - The interface defines what the service should do.
 - The implementation defines how it does it.
 - This allows controllers and other layers to depend on the contract, not the concrete implementation.
### 2️⃣ Flexibility & Maintainability
 - Easily switch implementations without modifying controllers.
 - Example: `UserServiceDbImpl` vs `UserServiceInMemoryImpl`.
### 3️⃣ Easier Unit Testing
 - Interfaces can be mocked in tests, allowing testing of controllers without hitting the database.
```java
@Mock
UserService userService;
when(userService.getAllUsers()).thenReturn(mockedUsers);
```
### 4️⃣ Supports Dependency Injection

 - Spring can inject the implementation automatically while the controller only depends on the interface:
```java
@RestController
public class UserController {
    private final UserService userService; // depends on interface

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```
***Conclusion:*** Using interfaces in the service layer is a clean architecture practice that ensures maintainability, testability, and scalability.