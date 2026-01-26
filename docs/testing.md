# Testing

## Default (Testcontainers)
Tests use Testcontainers to spin up Postgres automatically when Docker is available.

Run:
```text
.\mvnw test
```

## Local Postgres fallback
If Docker is not reachable from Java, you can run tests against a local Postgres instance.

PowerShell:
```text
$env:RCMS_TEST_USE_LOCAL_DB="true"
.\mvnw test
```

Optional overrides:
- `RCMS_TEST_DB_URL` (default `jdbc:postgresql://localhost:5432/rcms`)
- `RCMS_TEST_DB_USERNAME` (default `rcms`)
- `RCMS_TEST_DB_PASSWORD` (default `rcms_dev_password`)
