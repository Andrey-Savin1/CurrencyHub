package ru.savin.currencyhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.savin.currencyhub.model.ConversionRate;
import ru.savin.currencyhub.repository.ConversionRateRepository;
import ru.savin.currencyhub.repository.CurrencyRepository;
import ru.savin.currencyhub.repository.ProviderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversionRateService {

    private final ConversionRateRepository conversionRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ProviderRepository providerRepository;
    private final CurrencyService currencyService;
    private final RateProviderService rateProviderService;

    //@Scheduled(cron  = "${app.timers.rate}", timeUnit = TimeUnit.MINUTES)
    //lockAtLeastFor = "PT24H": Гарантирует, что задача не будет запущена снова в течение 24 часов.
    //lockAtMostFor = "PT25H": Ограничивает максимальное время блокировки до 25 часов (на случай зависания).

    @Scheduled(fixedRateString = "${app.timers.rate}", timeUnit = TimeUnit.MINUTES)
    // @SchedulerLock(name = "firstTask", lockAtLeastFor = "PT3M", lockAtMostFor = "PT5M")
    @Transactional
    public void saveRate() {
        log.debug("Starting scheduling");

        calculateAndSaveCrossRates();
        log.debug("Stop scheduling");
    }

    public void calculateAndSaveCrossRates() {
        log.debug("Start execution saveRate");

        var currencyPairs = currencyService.generateCurrenciesPair();
        var currencyCourses = rateProviderService.getRatesFromXML();

        for (var currencyPair : currencyPairs) {
            String source = currencyPair.a;
            String destination = currencyPair.b;

            //Получаем курс для обеих валют из пары
            BigDecimal sourceRate = currencyCourses.get(source);
            BigDecimal destinationRate = currencyCourses.get(destination);

            if (sourceRate != null && destinationRate != null) {
                // Рассчитываем кросс-курс
                BigDecimal crossRate = sourceRate.divide(destinationRate, 6, RoundingMode.HALF_UP);

                conversionRateRepository.save(ConversionRate.builder()
                        .createdAt(LocalDateTime.now())
                        .sourceCode(currencyRepository.findByCode(source))
                        .destinationCode(currencyRepository.findByCode(destination))
                        .rateBeginTime(LocalDate.now().atStartOfDay())
                        .rateEndTime(LocalDate.now().atStartOfDay().plusDays(1))
                        .rate(crossRate)
                        .providerCode(providerRepository.findByProviderCode("CBR"))
                        .multiplier(BigDecimal.ONE)
                        .systemRate(crossRate)
                        .build());
            }
        }
        log.debug("Completed execution saveRate. Total processed {} currency pair.", currencyPairs.size());
    }

    public ConversionRate getRate(String sourceCode, String destinationCode) {

        var source = currencyRepository.findByCode(sourceCode);
        var destination = currencyRepository.findByCode(destinationCode);
        return conversionRateRepository.findRateBySourceCodeAndDestinationCode(source, destination);
    }

}
