package ru.savin.currencyhub.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.savin.currencyhub.clients.CentralBankClient;
import ru.savin.currencyhub.parser.ValCurs;
import ru.savin.currencyhub.parser.Valute;
import ru.savin.currencyhub.service.RateProviderService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CurrenciesRateRestControllerV1IntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CurrenciesRateRestControllerV1IntegrationTest.class);
    private final CentralBankClient centralBankClient;
    private final TestRestTemplate restTemplate;
    private final RateProviderService rateProviderService;

    @Container
    static MockServerContainer mockServerContainer = new MockServerContainer(DockerImageName.parse(
            "mockserver/mockserver:5.15.0"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("CBURL", () -> String.format("http://%s:%s/scripts/XML_daily.asp",
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort()));
    }

    @BeforeEach
    void setUp() {
        // Настройка MockServer для имитации ответа от ЦБ
        MockServerClient mockServerClient = new MockServerClient(
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort()
        );

        String mockResponse = """
                <ValCurs Date="04.04.2025" name="Foreign Currency Market">
                    <Valute ID="R01010">
                        <NumCode>036</NumCode>
                        <CharCode>AUD</CharCode>
                        <Nominal>1</Nominal>
                        <Name>Австралийский доллар</Name>
                        <Value>52,8744</Value>
                    </Valute>
                    <Valute ID="R01235">
                        <NumCode>840</NumCode>
                        <CharCode>USD</CharCode>
                        <Nominal>1</Nominal>
                        <Name>Доллар США</Name>
                        <Value>90,1234</Value>
                    </Valute>
                </ValCurs>
                """;

        mockServerClient
                .when(
                        org.mockserver.model.HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/scripts/XML_daily.asp")
                )
                .respond(
                        org.mockserver.model.HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(mockResponse)
                                .withHeader("Content-Type", "application/xml;charset=UTF-8")
                );
    }

    @Test
    void testGetValCursFromCB() {

        String url = String.format("http://%s:%d/scripts/XML_daily.asp",
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort());

        // Логируем URL и ответ
        log.info("Request URL: {}", url);
        String response = restTemplate.getForObject(url, String.class);
        log.info("Response XML: {}", response);

        // Вызываем метод клиента
        ValCurs valCurs = centralBankClient.getValCursFromCB();
        log.debug("ValCurs {}", valCurs);

        // Проверяем результат
        assertThat(valCurs).isNotNull();
        assertThat(valCurs.getLocalDate()).isEqualTo("04.04.2025");
        assertThat(valCurs.getName()).isEqualTo("Foreign Currency Market");

        Valute audCurrency = valCurs.getValutes().stream()
                .filter(valute -> "AUD".equals(valute.getCharCode()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("AUD currency not found"));

        assertThat(audCurrency.getNumCode()).isEqualTo(36);
        assertThat(audCurrency.getNominal()).isEqualTo(1);
        assertThat(audCurrency.getName()).isEqualTo("Австралийский доллар");
        assertThat(audCurrency.getValue()).isEqualTo("52,8744");

        // Проверяем метод формирования валюты из XML
        var rate = rateProviderService.getRatesFromXML();

        assertThat(rate.get("AUD")).isEqualTo("52.8744");
        assertThat(rate.get("USD")).isEqualTo("90.1234");

    }

}
