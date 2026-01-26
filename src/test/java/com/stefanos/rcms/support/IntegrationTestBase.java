package com.stefanos.rcms.support;

import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
public abstract class IntegrationTestBase {

    private static final String DEFAULT_LOCAL_URL = "jdbc:postgresql://localhost:5432/rcms";
    private static final String DEFAULT_LOCAL_USERNAME = "rcms";
    private static final String DEFAULT_LOCAL_PASSWORD = "rcms_dev_password";

    private static final boolean USE_LOCAL_DB = getBooleanSetting(
        "RCMS_TEST_USE_LOCAL_DB",
        "rcms.test.useLocalDb",
        false
    );

    private static final PostgreSQLContainer<?> POSTGRES = startContainerIfNeeded();

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        if (POSTGRES != null) {
            registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
            registry.add("spring.datasource.username", POSTGRES::getUsername);
            registry.add("spring.datasource.password", POSTGRES::getPassword);
            registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
            return;
        }

        if (!USE_LOCAL_DB) {
            throw new IllegalStateException(
                "Docker is not available for tests. Set RCMS_TEST_USE_LOCAL_DB=true " +
                "to use a local Postgres instance, or fix Docker/Testcontainers."
            );
        }

        String url = getSetting("RCMS_TEST_DB_URL", "rcms.test.db.url", DEFAULT_LOCAL_URL);
        String username = getSetting("RCMS_TEST_DB_USERNAME", "rcms.test.db.username", DEFAULT_LOCAL_USERNAME);
        String password = getSetting("RCMS_TEST_DB_PASSWORD", "rcms.test.db.password", DEFAULT_LOCAL_PASSWORD);

        registry.add("spring.datasource.url", () -> url);
        registry.add("spring.datasource.username", () -> username);
        registry.add("spring.datasource.password", () -> password);
    }

    @AfterAll
    static void stopContainer() {
        if (POSTGRES != null) {
            POSTGRES.stop();
        }
    }

    private static PostgreSQLContainer<?> startContainerIfNeeded() {
        if (USE_LOCAL_DB || !isDockerAvailable()) {
            return null;
        }

        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("rcms")
            .withUsername("rcms")
            .withPassword("rcms");
        container.start();
        return container;
    }

    private static boolean isDockerAvailable() {
        try {
            return DockerClientFactory.instance().isDockerAvailable();
        } catch (Throwable ex) {
            return false;
        }
    }

    private static boolean getBooleanSetting(String envKey, String propertyKey, boolean defaultValue) {
        String value = getSetting(envKey, propertyKey, null);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    private static String getSetting(String envKey, String propertyKey, String defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value == null || value.isBlank()) {
            value = System.getenv(envKey);
        }
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
