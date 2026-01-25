# RCMS API

## Overview
All endpoints return JSON. Authentication uses JWT (Bearer token).

## Authentication
**POST** `/auth/login`

Request:
```json
{
  "username": "caseworker",
  "password": "case-pass"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

Use the token in the `Authorization` header:
```text
Authorization: Bearer <token>
```

## Roles
- `ADMIN`: full access
- `CASE_WORKER`: create/read/update/delete cases
- `AUDITOR`: read-only access to cases

## Cases
Base path: `/cases`

**POST** `/cases`  
Create a case (ADMIN, CASE_WORKER).

Request:
```json
{
  "externalReference": "CASE-1001",
  "title": "Initial intake",
  "description": "First case",
  "status": "OPEN",
  "assignedTo": "caseworker-1"
}
```

Response:
```json
{
  "id": 1,
  "externalReference": "CASE-1001",
  "title": "Initial intake",
  "description": "First case",
  "assignedTo": "caseworker-1",
  "status": "OPEN",
  "createdAt": "2026-01-21T20:46:47.4698322-05:00",
  "updatedAt": "2026-01-21T20:46:47.4698322-05:00"
}
```

**GET** `/cases`  
List all cases (ADMIN, CASE_WORKER, AUDITOR).

Optional query params:
`status`, `externalReference`, `assignedTo`, `page`, `size`, `sort`

Example:
`/cases?assignedTo=caseworker-1&status=OPEN&page=0&size=10&sort=createdAt,desc`

**GET** `/cases/{id}`  
Fetch a case by id (ADMIN, CASE_WORKER, AUDITOR).

**PUT** `/cases/{id}`  
Update a case (ADMIN, CASE_WORKER).

Request:
```json
{
  "title": "Updated title",
  "description": "Updated",
  "status": "IN_PROGRESS",
  "assignedTo": "caseworker-2"
}
```

**DELETE** `/cases/{id}`  
Delete a case (ADMIN, CASE_WORKER).

## Errors
Validation errors return **400** with details.
Auth errors return **401**.
Forbidden actions return **403**.
Missing resources return **404**.
