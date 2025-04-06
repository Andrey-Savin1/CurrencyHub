package ru.savin.currencyhub.repository;

import org.springframework.data.repository.CrudRepository;
import ru.savin.currencyhub.model.ConversionRate;
import ru.savin.currencyhub.model.Currency;

import java.time.LocalDateTime;

public interface ConversionRateRepository extends CrudRepository<ConversionRate, Long> {

    ConversionRate findRateBySourceCodeAndDestinationCode(Currency sourceCode, Currency destinationCode);

}
