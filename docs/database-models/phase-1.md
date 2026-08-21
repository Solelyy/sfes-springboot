# Database Models

## Phase 1 — Authentication & Account Management

### 1. Employee

| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | PK |
| employeeId | VARCHAR | UNIQUE, NOT NULL |
| firstName | VARCHAR | NOT NULL |
| middleName | VARCHAR | NULL |
| lastName | VARCHAR | NOT NULL |
| createdAt | TIMESTAMP | NOT NULL |
| updatedAt | TIMESTAMP | NOT NULL |

---

### 2. User

| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | PK |
| email | VARCHAR | UNIQUE, NOT NULL |
| hashedPassword | VARCHAR | NOT NULL |
| role | ENUM | NOT NULL |
| status | ENUM | NOT NULL |
| failedLoginAttempts | INT | NOT NULL, DEFAULT 0 |
| lockedUntil | TIMESTAMP | NULL |
| lastLogin | TIMESTAMP | NULL |
| employeeId | BIGINT | FK, UNIQUE, NULL |
| createdAt | TIMESTAMP | NOT NULL |
| updatedAt | TIMESTAMP | NOT NULL |

#### Role

- `SUPER_ADMIN`
- `REGISTRAR`
- `GUIDANCE`

#### Status

- `PENDING`
- `ACTIVE`
- `INACTIVE`
- `REMOVED`

---

### 3. AccountInvitation

| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | PK |
| userId | BIGINT | FK, NOT NULL |
| tokenHash | VARCHAR | NOT NULL |
| expiresAt | TIMESTAMP | NOT NULL |
| acceptedAt | TIMESTAMP | NULL |
| createdAt | TIMESTAMP | NOT NULL |

---

## Relationships

```text
Employee
    │
    │ 1 : 0..1
    ▼
User
    │
    │ 1 : 0..1
    ▼
AccountInvitation