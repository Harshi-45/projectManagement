# Authentication & Authorization System

## Overview
Complete JWT-based authentication system with role-based access control (RBAC), token refresh mechanism, and API rate limiting.

## Features

### 1. JWT Authentication
- **Access Tokens**: Short-lived (15 minutes) for API access
- **Refresh Tokens**: Long-lived (7 days) stored in Redis
- **Token Structure**: Contains userId, email, and role

### 2. Role-Based Access Control (RBAC)
- **OWNER**: Full system access, can create users
- **COLLABORATOR**: Can edit projects and collaborate
- **VIEWER**: Read-only access

### 3. Token Refresh Mechanism
- Refresh tokens stored in Redis with automatic expiration
- Old refresh tokens revoked when new ones are issued
- Token rotation for enhanced security

### 4. API Rate Limiting
- Configurable requests per minute (default: 60)
- IP-based rate limiting using Redis
- Returns 429 Too Many Requests when exceeded

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- BCrypt hashed
    name VARCHAR(255),
    role VARCHAR(32) NOT NULL,  -- OWNER, COLLABORATOR, VIEWER
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_by VARCHAR(255) NULL,
    modified_at TIMESTAMP NULL,
    deleted TINYINT(1) DEFAULT 0
);
```

### Owner User
Created via Flyway migration (`V3__create_owner_user.sql`):
- **Email**: `owner@projectmanagement.com`
- **Password**: `admin123` (BCrypt hashed)
- **Role**: `OWNER`

## API Endpoints

### Authentication

#### 1. Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "owner@projectmanagement.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

#### 2. Refresh Token
```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGci..."
}
```

**Response:** Same as login (new tokens)

### User Management (Requires OWNER role)

#### 1. Create User
```http
POST /api/v1/users
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe",
  "role": "COLLABORATOR"
}
```

**Valid Roles:** `OWNER`, `COLLABORATOR`, `VIEWER`

#### 2. Update User
```http
PUT /api/v1/users/{id}
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "name": "Jane Doe",
  "role": "VIEWER"
}
```

## Architecture

### Components

1. **JwtUtil**: Token generation and validation
2. **AuthenticationService**: Login and refresh logic
3. **RefreshTokenService**: Redis-based token storage
4. **JwtAuthFilter**: JWT validation filter
5. **RateLimitingFilter**: API rate limiting
6. **RoleAuthorizationAspect**: RBAC enforcement
7. **AuthContext**: Current user context utility

### Security Flow

```
Request → RateLimitingFilter → JwtAuthFilter → SecurityConfig → Controller
                                                      ↓
                                            RoleAuthorizationAspect
```

## Configuration

### application.yml
```yaml
spring:
  app:
    security:
      jwt:
        secret: ${JWT_SECRET:dev-secret}
        access-expiration: 900000     # 15 mins
        refresh-expiration: 604800000 # 7 days
    rate-limit:
      enabled: true
      requests-per-minute: 60
```

## Usage Flow

### 1. Initial Setup
1. Run Flyway migrations (creates owner user)
2. Start Redis server
3. Start application

### 2. Login as Owner
```bash
POST /api/v1/auth/login
{
  "email": "owner@projectmanagement.com",
  "password": "admin123"
}
```

### 3. Create Users
Use the access token to create users:
```bash
POST /api/v1/users
Authorization: Bearer {accessToken}
{
  "email": "collaborator@example.com",
  "password": "pass123",
  "name": "Collaborator User",
  "role": "COLLABORATOR"
}
```

### 4. Token Refresh
When access token expires:
```bash
POST /api/v1/auth/refresh
{
  "refreshToken": "{refreshToken}"
}
```

## RBAC Implementation

### Using @RequireRole Annotation
```java
@PostMapping("/users")
@RequireRole({Role.OWNER})
public ResponseEntity<User> createUser(@RequestBody User user) {
    // Only OWNER can access this
}
```

### Multiple Roles
```java
@RequireRole({Role.OWNER, Role.COLLABORATOR})
```

## Rate Limiting

- **Default**: 60 requests per minute per IP
- **Configurable**: Via `application.yml`
- **Response**: 429 Too Many Requests with Retry-After header
- **Skipped for**: Actuator endpoints

## Security Features

1. **Password Hashing**: BCrypt with salt
2. **Token Expiration**: Short-lived access tokens
3. **Token Rotation**: New refresh token on each refresh
4. **Token Revocation**: Old tokens invalidated
5. **Role Validation**: Enforced at method level
6. **Input Validation**: Jakarta Validation annotations

## Error Handling

- **401 Unauthorized**: Missing or invalid token
- **403 Forbidden**: Insufficient permissions
- **429 Too Many Requests**: Rate limit exceeded
- **400 Bad Request**: Validation errors

## Testing

### Test Login
```bash
curl -X POST http://localhost:7000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"owner@projectmanagement.com","password":"admin123"}'
```

### Test User Creation
```bash
curl -X POST http://localhost:7000/api/v1/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {accessToken}" \
  -d '{"email":"test@example.com","password":"test123","name":"Test User","role":"VIEWER"}'
```

## Dependencies

- Spring Security
- Spring Data Redis
- JWT (io.jsonwebtoken)
- BCrypt Password Encoder
- AspectJ for RBAC

## Notes

- **Redis Required**: For refresh token storage and rate limiting
- **BCrypt Passwords**: All passwords must be BCrypt hashed
- **Token Storage**: Refresh tokens in Redis, access tokens stateless
- **Role Hierarchy**: OWNER > COLLABORATOR > VIEWER

