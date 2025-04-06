package ru.savin.currencyhub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.savin.currencyhub.clients.CentralBankClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RateProviderService {

    @Autowired
    private CentralBankClient bankClient;

    public Map<String, BigDecimal> getRatesFromXML() {

        var response = bankClient.getValCursFromCB();

        Map<String, BigDecimal> currencyCourse = new HashMap<>();
        if (response != null && response.getValutes() != null) {
            for (var valute : response.getValutes()) {
                BigDecimal value = new BigDecimal(valute.getValue().replace(",","."));
                BigDecimal nominal = new BigDecimal(valute.getNominal());
                BigDecimal result = value.divide(nominal, 4, RoundingMode.HALF_UP);

                currencyCourse.put(valute.getCharCode(), result);
            }
        }
        //На выходе получаем стоимость одной единицы валюты в рублях
        return currencyCourse;
    }

}
