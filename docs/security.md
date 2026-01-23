# RCMS Security

## Overview
RCMS uses stateless authentication with JWT bearer tokens.

## Token Flow
1) Client posts credentials to `/auth/login`.
2) Server verifies user + password.
3) Server returns a signed JWT.
4) Client sends `Authorization: Bearer <token>` on every request.

## Roles
- `ADMIN`: full access to all case operations
- `CASE_WORKER`: create/read/update/delete cases
- `AUDITOR`: read-only access to cases

## Endpoint Rules
- `POST /auth/login` → public
- `GET /health` → public
- `GET /cases/**` → ADMIN, CASE_WORKER, AUDITOR
- `POST /cases` → ADMIN, CASE_WORKER
- `PUT /cases/**` → ADMIN, CASE_WORKER
- `DELETE /cases/**` → ADMIN, CASE_WORKER

## Why JWT
- Stateless: no server sessions required.
- Scales: easy to deploy behind a load balancer.
- Auditable: token claims carry identity/roles.

## Notes
- Secrets must be 32+ characters and kept private.
- Tokens are short‑lived (1 hour by default).
- Rotate secrets in production and use environment variables.
