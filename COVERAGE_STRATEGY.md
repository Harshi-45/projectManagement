# Code Coverage Strategy

## Overview
This document explains what packages are included/excluded from code coverage and why.

## Coverage Target
- **Minimum Coverage**: 70% (configurable in `build.gradle`)
- **Focus**: Business logic and critical functionality

## Included Packages (Must Have Tests)

### ✅ `controller/**`
- **Why**: REST endpoints handle request validation, authentication, and response formatting
- **Example**: `AuthController`, `ProjectController`, `UserController`
- **Test Type**: Integration tests with `@WebMvcTest` or `@SpringBootTest`

### ✅ `service/**`
- **Why**: Core business logic - the heart of the application
- **Example**: `ProjectService`, `UserService`
- **Test Type**: Unit tests with mocked dependencies

### ✅ `repository/**`
- **Why**: Data access layer - queries and persistence logic
- **Example**: `ProjectRepository`, `UserRepository`
- **Test Type**: Integration tests with Testcontainers

### ✅ `Util/**`
- **Why**: Critical utility classes with business logic
- **Example**: 
  - `JwtUtil` - JWT token generation/validation
  - `AuthContext` - Authentication context extraction
  - `JwtAuthFilter` - Security filter logic
- **Test Type**: Unit tests

## Excluded Packages (No Coverage Required)

### ❌ `config/**`
- **Why**: Spring configuration classes - framework setup, no business logic
- **Example**: `SecurityConfig`, `PasswordConfig`, `AuditConfig`

### ❌ `model/**`
- **Why**: DTOs and data transfer objects - just getters/setters
- **Example**: `Project`, `User`, `TokenResponse`

### ❌ `entity/**`
- **Why**: JPA entities - data models with annotations, no business logic
- **Example**: `ProjectEntity`, `UserEntity`, `JobEntity`

### ❌ `mapper/**`
- **Why**: MapStruct generated code - auto-generated mappers
- **Example**: `ProjectMapper`, `UserMapper` (generated implementations)

### ❌ `exception/**`
- **Why**: Simple exception classes - just constructors
- **Example**: `BaseException`, `DataValidationException`

### ❌ `ProjectManagementApplication`
- **Why**: Main application class - just starts Spring Boot

## Running Coverage Reports

### Generate Report
```bash
./gradlew test jacocoTestReport
```

### View HTML Report
Open: `build/reports/jacoco/test/html/index.html`

### View XML Report
Location: `build/reports/jacoco/test/jacocoTestReport.xml`

### Verify Coverage Threshold
```bash
./gradlew jacocoTestCoverageVerification
```

## Coverage Metrics

The coverage report shows:
- **Line Coverage**: Percentage of executable lines covered
- **Branch Coverage**: Percentage of branches (if/else) covered
- **Method Coverage**: Percentage of methods covered
- **Class Coverage**: Percentage of classes covered

## Best Practices

1. **Focus on Business Logic**: Prioritize testing services and utilities
2. **Integration Tests**: Use Testcontainers for repository and controller tests
3. **Mock External Dependencies**: Mock databases, external APIs in unit tests
4. **Test Edge Cases**: Don't just test happy paths
5. **Maintain Coverage**: Keep coverage above 70% as code grows

## Current Coverage Status

Run `./gradlew test jacocoTestReport` to see current coverage metrics.

