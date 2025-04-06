package ru.savin.currencyhub.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.savin.currencyhub.parser.ValCurs;

@Component
@Slf4j
@RequiredArgsConstructor
public class CentralBankClient {

    private final RestTemplate restTemplate;

    @Value("${CBURL}")
    private String CBURl;

    public ValCurs getValCursFromCB() {
        ValCurs response = restTemplate.getForObject(CBURl, ValCurs.class);
        log.debug("Exchange rates of the Central Bank against the ruble: {}", response);

        return response;
    }
}
