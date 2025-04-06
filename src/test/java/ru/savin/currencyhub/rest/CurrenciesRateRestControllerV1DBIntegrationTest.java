package ru.savin.currencyhub.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CurrenciesRateRestControllerV1DBIntegrationTest {

    private final TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testuser");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        // Используем специальный URL для Testcontainers
        String jdbcUrl = "jdbc:tc:postgresql:15:///testdb?TC_TMPFS=/testtmpfs:rw";
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.locations", () -> "classpath:/db/migration-test");
    }

    @Test
    public void testCurrencyRateAfterScheduler() throws InterruptedException {
        // Ждем появления данных в базе контейнера
        Thread.sleep(15000);

        // Проверяем курс валют
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/rates/USD/EUR", String.class);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(0.90635, Double.parseDouble(response.getBody()));
    }
}
