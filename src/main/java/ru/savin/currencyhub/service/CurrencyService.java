package ru.savin.currencyhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.savin.currencyhub.repository.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Pair<String, String>> generateCurrenciesPair() {
        List<String> currencies = currencyRepository.findAllCodeByActiveIsTrue();
        log.debug("Active currencies for calculating the exchange rate: {}", currencies);

        List<Pair<String, String>> pairs = new ArrayList<>();
        for (var first : currencies) {
            for (var second : currencies) {
                if (!first.equals(second)) {
                    pairs.add(new Pair<>(first, second));
                }
            }
        }
        log.debug("Final currency pairs: {}", pairs);
        return pairs;
    }

}
