package ru.savin.currencyhub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.savin.currencyhub.clients.CentralBankClient;
import ru.savin.currencyhub.parser.ValCurs;
import ru.savin.currencyhub.parser.Valute;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateProviderServiceTest {

    @Mock
    private CentralBankClient bankClient;

    @InjectMocks
    private RateProviderService rateProviderService;

    @Test
    public void testGetRatesFromXML() {

        ValCurs mockResponse = new ValCurs();
        mockResponse.setLocalDate("04.04.2025");
        mockResponse.setName("Foreign Currency Market");
        mockResponse.setValutes(List.of(
                        new Valute("R01235", 840, "USD", 1, "Доллар США", "84,3830", "84,3830"),
                        new Valute("R01239", 978, "EUR", 1, "Евро", "93,1608", "93,1608")
                ));

        when(bankClient.getValCursFromCB()).thenReturn(mockResponse);

        Map<String, BigDecimal> result = rateProviderService.getRatesFromXML();

        Map<String, BigDecimal> expected = new HashMap<>();
        expected.put("USD", new BigDecimal("84.3830"));
        expected.put("EUR", new BigDecimal("93.1608"));

        assertEquals(expected, result);

    }


}