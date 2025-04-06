package ru.savin.currencyhub.service;

import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.savin.currencyhub.repository.CurrencyRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void generateCurrenciesPair() {
        List<String> activeCurrencies = List.of("USD", "EUR", "GBP");

        when(currencyRepository.findAllCodeByActiveIsTrue()).thenReturn(activeCurrencies);
        List<Pair<String, String>> result = currencyService.generateCurrenciesPair();

        List<Pair<String, String>> expectedPairs = List.of(
                new Pair<>("USD", "EUR"),
                new Pair<>("USD", "GBP"),
                new Pair<>("EUR", "USD"),
                new Pair<>("EUR", "GBP"),
                new Pair<>("GBP", "USD"),
                new Pair<>("GBP", "EUR")
        );
        assertEquals(expectedPairs, result);
    }
}