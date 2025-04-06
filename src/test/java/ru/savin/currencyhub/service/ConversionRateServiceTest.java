package ru.savin.currencyhub.service;

import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.savin.currencyhub.model.ConversionRate;
import ru.savin.currencyhub.model.Currency;
import ru.savin.currencyhub.model.RateProvider;
import ru.savin.currencyhub.repository.ConversionRateRepository;
import ru.savin.currencyhub.repository.CurrencyRepository;
import ru.savin.currencyhub.repository.ProviderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversionRateServiceTest {

    @Mock
    private CurrencyService currencyService;
    @Mock
    private RateProviderService rateProviderService;
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private ConversionRateRepository conversionRateRepository;
    @Mock
    private ProviderRepository providerRepository;
    @InjectMocks
    private ConversionRateService currencyHubService;
    @Test
    void calculateAndSaveCrossRates() {

        List<Pair<String, String>> currencyPairs = List.of(
                new Pair<>("USD", "EUR"),
                new Pair<>("EUR", "USD")
        );

        Map<String, BigDecimal> currencyCourses = Map.of(
                "USD", new BigDecimal("80.00"),
                "EUR", new BigDecimal("90.00")
        );

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

        when(currencyService.generateCurrenciesPair()).thenReturn(currencyPairs);
        when(rateProviderService.getRatesFromXML()).thenReturn(currencyCourses);

        when(currencyRepository.findByCode("USD")).thenReturn(usd);
        when(currencyRepository.findByCode("EUR")).thenReturn(eur);
        when(providerRepository.findByProviderCode("CBR")).thenReturn(provider);

        currencyHubService.calculateAndSaveCrossRates();
        verify(conversionRateRepository, times(2)).save(any(ConversionRate.class));

        ArgumentCaptor<ConversionRate> captor = ArgumentCaptor.forClass(ConversionRate.class);
        verify(conversionRateRepository, times(2)).save(captor.capture());

        List<ConversionRate> savedRates = captor.getAllValues();

        ConversionRate usdToEur = savedRates.get(0);
        assertEquals(new BigDecimal("0.888889"), usdToEur.getRate());
        assertEquals("EUR", usdToEur.getSourceCode().getCode());

        ConversionRate eurToUsd = savedRates.get(1);
        assertEquals(new BigDecimal("1.125000"), eurToUsd.getRate());
        assertEquals("EUR", eurToUsd.getDestinationCode().getCode());


    }
}