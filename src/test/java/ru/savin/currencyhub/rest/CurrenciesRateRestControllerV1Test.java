package ru.savin.currencyhub.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.savin.currencyhub.model.ConversionRate;
import ru.savin.currencyhub.model.Currency;
import ru.savin.currencyhub.model.RateProvider;
import ru.savin.currencyhub.restcontroller.v1.CurrenciesRateRestControllerV1;
import ru.savin.currencyhub.service.ConversionRateService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrenciesRateRestControllerV1.class)
class CurrenciesRateRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConversionRateService conversionRateService;

    @Test
    public void testGetCurrencyCourse_Success() throws Exception {

        Currency usd = new Currency();
        usd.setId(1L);
        usd.setCode("USD");
        usd.setIsoCode("840");
        usd.setDescription("Доллар США");
        usd.setActive(true);
        usd.setSymbol("$");
        usd.setScale(2);

        Currency eur = new Currency();
        usd.setId(2L);
        usd.setCode("EUR");
        usd.setIsoCode("978");
        usd.setDescription("Евро");
        usd.setActive(true);
        usd.setSymbol("€");
        usd.setScale(2);

        RateProvider provider = new RateProvider();
        provider.setActive(true);
        provider.setDescription("Центробанк России");
        provider.setProviderCode("CBR");
        provider.setCreatedAt(LocalDateTime.now());
        provider.setModifiedAt(LocalDateTime.now());
        provider.setPriority(10);
        provider.setDefaultMultiplier(BigDecimal.ONE);

        ConversionRate rate = new ConversionRate();
        rate.setId(1L);
        rate.setSourceCode(usd);
        rate.setDestinationCode(eur);
        rate.setId(1L);
        rate.setRate(BigDecimal.valueOf(0.905778));
        rate.setProviderCode(provider);
        rate.setMultiplier(BigDecimal.ONE);
        rate.setSystemRate(BigDecimal.valueOf(0.905778));

        when(conversionRateService.getRate("USD", "EUR")).thenReturn(rate);

        mockMvc.perform(get("/api/v1/rate/{source}/{destination}", "USD", "EUR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(0.905778));

    }
}