package ru.savin.currencyhub.restcontroller.v1;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.savin.currencyhub.handler.CustomBusinessException;
import ru.savin.currencyhub.service.ConversionRateService;

@RestController
@RequestMapping(value = "/api/v1/rates")
@Slf4j
@RequiredArgsConstructor
public class CurrenciesRateRestControllerV1 {

    private final ConversionRateService conversionRateService;

    @GetMapping("/{source}/{destination}")
    public ResponseEntity<?> getCurrencyCourse(@PathVariable String source, @PathVariable String destination) {

        var rate = conversionRateService.getRate(source, destination);
        if (rate == null) {
            throw new CustomBusinessException("RATE_NOT_FOUND", "Exchange rate not found for pair: "
                    + source + "/" + destination);
        }
        return ResponseEntity.ok(rate.getRate());
    }

    @PostMapping("/update-courses")
    public ResponseEntity<String> forceUpdateCurrencyRates() {
        conversionRateService.calculateAndSaveCrossRates();
        log.info("Currency courses have been successfully updated.");
        return ResponseEntity.ok("Currency courses have been successfully updated.");
    }

}
